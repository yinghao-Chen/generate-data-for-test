package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.service.RandomData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/14
 */
@Service
public class RandomDataImpl implements RandomData {

    @Override
    public Map<String, Object> generate(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(map.size() * 4 / 3 + 1);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            explain(entry, result);
        }
        return result;
    }

    private void explain(Map.Entry<String, Object> entry, Map<String, Object> result) {
        // 解析key
        String[] keys = entry.getKey().split("\\|");
        String field = keys[0], rule = null;
        if(keys.length == 2) {
            rule = keys[1];
        }

        // 解析value
        Object value = entry.getValue();
        if(value instanceof String) {

        }
    }

}
