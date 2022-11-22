package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.DataSource;
import cn.cyh.generatedata.api.enums.DataSourceFunc;
import cn.cyh.generatedata.api.vo.GenToDataSourceSaveVO;
import cn.cyh.generatedata.api.vo.GenToDataSourceVO;
import cn.cyh.generatedata.service.DataSourceStrategy;
import cn.cyh.generatedata.service.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author cyh
 * @date 2022/11/22
 */
@Slf4j
@Service
@DataSourceFunc(dataSource = DataSource.MYSQL)
public class MysqlDataSourceImpl implements DataSourceStrategy {

    private final RandomData randomData;
    @Autowired
    MysqlDataSourceImpl(RandomData randomData) {
        this.randomData = randomData;
    }

    @Override
    public boolean test(GenToDataSourceVO vo) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            Connection connection = DriverManager.getConnection(vo.getUrl(), vo.getUsername(), vo.getPassword());
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select 1");
            if(resultSet != null) {
                log.info("mysql 连接成功：{}", vo.getUrl());
            }
            statement.close();
            connection.close();
            return true;
        } catch (Exception e) {
            log.error("mysql连接失败", e);
        }
        return false;
    }

    @Override
    public int saveData(GenToDataSourceSaveVO vo) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            Connection connection = DriverManager.getConnection(vo.getMeta().getUrl(),
                    vo.getMeta().getUsername(), vo.getMeta().getPassword());
            Statement statement = connection.createStatement();

            // 获取表字段信息
            ResultSet resultSet = statement.executeQuery("select * from " + vo.getMeta().getTable() + " limit 1");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(columnCount == 0) {
                return 0;
            }
            Set<String> fields = new HashSet<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                fields.add(columnName.toLowerCase());
            }

            // 剔除没有的字段
            Map<String, Object> map = new HashMap<>(vo.getRuleData().size() * 4 / 3 + 1);
            for (Map.Entry<String, Object> entry : vo.getRuleData().entrySet()) {
                String s = entry.getKey().split("\\|")[0];
                if(fields.contains(s) || fields.contains(s.toUpperCase())) {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            if(map.size() == 0) {
                return 0;
            }

            // 组装sql
            List<String> sqlList = new ArrayList<>(vo.getMeta().getCount());
            final String sqlPrefix = "insert into "+ vo.getMeta().getTable();
            for (Integer i = 0; i < vo.getMeta().getCount(); i++) {
                Map<String, Object> dataMap = randomData.generate(map);

                StringJoiner sqlField = new StringJoiner(",");
                StringJoiner sqlData = new StringJoiner(",");
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    sqlField.add("`" + entry.getKey() + "`");
                    sqlData.add("'" + entry.getValue() + "'");
                }
                String sql = sqlPrefix + " (" + sqlField.toString() + ") values " +
                        "(" + sqlData.toString() + ")";
                sqlList.add(sql);
            }

            // 批量插入
            final int batchCount = 1000;
            StringJoiner sj = new StringJoiner(",");
            for (int i = 0; i < sqlList.size(); i++) {
                String sql = sqlList.get(i);
                if(i == 0) {
                    sj.add(sql);
                }
                if(i % batchCount > 0) {
                    sj.add(sql.substring(sql.indexOf("values") + "values".length()));
                }
                boolean b = (i > 0 && i % batchCount == 0) || i == sqlList.size() - 1;
                if(b) {
                    statement.execute(sj.toString());
                }

                if(i > 0 && i % batchCount == 0) {
                    sj = new StringJoiner(",");
                    sj.add(sql);
                }
            }

            statement.closeOnCompletion();
            connection.close();
            return sqlList.size();
        } catch (Exception e) {
            log.error("mysql连接失败", e);
        }
        return 0;
    }

}
