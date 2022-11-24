package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.DataSource;
import cn.cyh.generatedata.api.enums.DataSourceFunc;
import cn.cyh.generatedata.api.vo.GenToDataSourceSaveVO;
import cn.cyh.generatedata.api.vo.GenToDataSourceVO;
import cn.cyh.generatedata.service.DataSourceStrategy;
import cn.cyh.generatedata.service.RandomData;
import cn.cyh.generatedata.utils.FieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author cyh
 * @date 2022/11/24
 */
@Slf4j
@Service
@DataSourceFunc(dataSource = DataSource.ORACLE)
public class OracleDataSourceImpl implements DataSourceStrategy {

    private final RandomData randomData;
    @Autowired
    OracleDataSourceImpl(RandomData randomData) {
        this.randomData = randomData;
    }

    @Override
    public boolean test(GenToDataSourceVO vo) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

            Connection connection = DriverManager.getConnection(vo.getUrl(), vo.getUsername(), vo.getPassword());
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select 1");
            if(resultSet != null) {
                log.info("oracle 连接成功：{}", vo.getUrl());
            }
            statement.close();
            connection.close();
            return true;
        } catch (Exception e) {
            log.error("oracle连接失败", e);
        }
        return false;
    }

    @Override
    public int saveData(GenToDataSourceSaveVO vo) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

            Connection connection = DriverManager.getConnection(vo.getMeta().getUrl(),
                    vo.getMeta().getUsername(), vo.getMeta().getPassword());
            Statement statement = connection.createStatement();

            String[] db = vo.getMeta().getTable().split(":");
            // 获取表字段信息
            ResultSet resultSet = statement.executeQuery(
                    String.format("select * from \"%s\".\"%s\" where rownum <= 1", db[0].trim(), db[1].trim()));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(columnCount == 0) {
                return 0;
            }
            Set<String> fields = new HashSet<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                fields.add(columnName);
            }

            // 剔除没有的字段
            Map<String, Object> map = new HashMap<>(vo.getRuleData().size() * 4 / 3 + 1);
            for (Map.Entry<String, Object> entry : vo.getRuleData().entrySet()) {
                String s = entry.getKey().split("\\|")[0];
                // 大小写敏感处理
                String column = FieldUtil.existsFields(fields, s);
                if(column != null) {
                    map.put(column + entry.getKey().substring(s.length()), entry.getValue());
                }
            }
            if(map.size() == 0) {
                return 0;
            }

            // 批量插入
            final int batchCount = 1000;
            StringJoiner bulk = new StringJoiner("\n");
            bulk.add("begin");
            for (Integer i = 0; i < vo.getMeta().getCount(); i++) {
                Map<String, Object> dataMap = randomData.generate(map);
                StringJoiner data = new StringJoiner(",");
                StringJoiner columns = new StringJoiner(",");
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    columns.add("\""+ entry.getKey() +"\"");
                    data.add("'"+ entry.getValue() +"'");
                }
                bulk.add("insert into \""+ db[0].trim() +"\".\"" + db[1].trim()
                        + "\" (" + columns.toString() + ") values (" + data.toString() + ");");

                boolean b = (i > 0 && i % batchCount == 0) || i == vo.getMeta().getCount() - 1;
                if(b) {
                    bulk.add("end;");
                    statement.execute(bulk.toString());
                    bulk = new StringJoiner("\n");
                }
            }

            statement.closeOnCompletion();
            connection.close();
            return vo.getMeta().getCount();
        } catch (Exception e) {
            log.error("oracle操作失败", e);
        }
        return 0;
    }

}
