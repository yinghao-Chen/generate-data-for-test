package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.DataSource;
import cn.cyh.generatedata.api.enums.DataSourceFunc;
import cn.cyh.generatedata.api.vo.GenToDataSourceSaveVO;
import cn.cyh.generatedata.api.vo.GenToDataSourceVO;
import cn.cyh.generatedata.service.DataSourceStrategy;
import cn.cyh.generatedata.service.OkHttpCli;
import cn.cyh.generatedata.service.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author cyh
 * @date 2022/11/23
 */
@Slf4j
@Service
@DataSourceFunc(dataSource = DataSource.ELASTIC_SEARCH)
public class EsDataSourceImpl implements DataSourceStrategy {

    private final RandomData randomData;
    private final OkHttpCli okHttpCli;
    @Autowired
    EsDataSourceImpl(RandomData randomData, OkHttpCli okHttpCli) {
        this.randomData = randomData;
        this.okHttpCli = okHttpCli;
    }

    @Override
    public boolean test(GenToDataSourceVO vo) {
        try{
            String url = String.format("http://%s:9200/_cluster/health", vo.getUrl().trim());
            okHttpCli.doGet(url);
            return true;
        } catch (Exception e) {
            log.error("es连接失败", e);
        }
        return false;
    }

    @Override
    public int saveData(GenToDataSourceSaveVO vo) {
        try{
            String url = String.format("http://%s:9200/_bulk", vo.getMeta().getUrl().trim());

            // 生成数据,计数
            final int cnt = 10000;
            StringJoiner bulk = new StringJoiner("\n");
            for (Integer i = 0; i < vo.getMeta().getCount(); i++) {
                Map<String, Object> dataMap = randomData.generate(vo.getRuleData());

                bulk.add("{\"create\":{\"_index\":\""+ vo.getMeta().getTable()
                        +"\",\"_type\":\""+ vo.getMeta().getType()
                        +"\",\"_id\":\""+ UUID.randomUUID().toString().replace("-", "") + "\"}}");
                StringBuilder data = new StringBuilder();
                data.append("{");
                int i1 = 0;
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    if(i1 > 0) {
                        data.append(",");
                    }
                    data.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                    i1++;
                }
                data.append("}");
                bulk.add(data.toString());

                boolean b = (i > 0 && i % cnt == 0) || i == vo.getMeta().getCount() - 1;
                if(b) {
                    bulk.add("");
                    okHttpCli.doPostJson(url, bulk.toString());
                    bulk = new StringJoiner("\n");
                }
            }

            return vo.getMeta().getCount();
        } catch (Exception e) {
            log.error("es saveData error", e);
        }
        return 0;
    }

}
