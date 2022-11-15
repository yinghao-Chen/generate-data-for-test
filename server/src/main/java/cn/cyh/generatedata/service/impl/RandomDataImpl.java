package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.service.RandomData;
import cn.cyh.generatedata.utils.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/14
 */
@Slf4j
@Service
public class RandomDataImpl implements RandomData {

    @Override
    public Map<String, Object> generate(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>(map.size() * 4 / 3 + 1);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            GenerateUtil.generate(entry, result, map);
        }
        return result;
    }

}
