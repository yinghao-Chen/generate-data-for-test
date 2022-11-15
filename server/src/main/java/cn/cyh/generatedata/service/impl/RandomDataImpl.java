package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.Method;
import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.rereg.exception.RegexpIllegalException;
import cn.cyh.generatedata.rereg.exception.TypeNotMatchException;
import cn.cyh.generatedata.rereg.exception.UninitializedException;
import cn.cyh.generatedata.rereg.model.OrdinaryNode;
import cn.cyh.generatedata.service.RandomData;
import cn.cyh.generatedata.utils.SfzhUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

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
            String v = (String) value;
            // 函数
            if(v.startsWith("@")) {
                if(Method.SFZH.getValue().equals(v)) {
                    result.put(field, SfzhUtil.generateSfzh());
                }
            } else {
                // 普通字符串
                if(rule == null) {
                    result.put(field, v);
                } else if(Rule.NUM_RANGE.getValue().matcher(rule).matches()) {
                    result.put(field, getResult(v, getNumRange(rule)));
                } else if(Rule.NUM.getValue().matcher(rule).matches()) {
                    result.put(field, getResult(v, Integer.parseInt(rule)));
                } else if(Rule.ONE_MORE.getValue().matcher(rule).matches()) {
                    Random random = new Random();
                    result.put(field, getResult(v, random.nextInt(5)));
                } else if(Rule.FLOAT.getValue().matcher(rule).matches()) {
                    String[] ol = rule.split("\\.");
                    result.put(field, getResult(v, getNumRange(ol[0])) + "." + getResult(v, getNumRange(ol[1])));
                } else if(Rule.REGEXP.getValue().matcher(rule).matches()) {
                    try {
                        result.put(field, new OrdinaryNode(v).random());
                    } catch (UninitializedException | RegexpIllegalException | TypeNotMatchException e) {
                        log.error("RandomData 'Rule.REGEXP generate' Exception happened", e);
                    }
                }
            }
        } else if (value instanceof Boolean) {
            Boolean v = (Boolean) value;
            if(rule == null) {
                result.put(field, v);
            } else if(Rule.NUM.getValue().matcher(rule).matches()) {
                int n = Integer.parseInt(rule);
                result.put(field, n % 2 == 0 ? v : !v);
            }
        } else if(value instanceof Map) {

        }
        if(!result.containsKey(field)) {
            result.put(field, value);
        }
    }

    private int getNumRange(String rule) {
        String[] arr = rule.split("-");
        int min = Integer.parseInt(arr[0]);
        int max = Integer.parseInt(arr[1]);
        Random random = new Random();
        return random.nextInt(max) + Math.abs(max-min);
    }

    private String getResult(String v, int num) {
        StringBuilder sb = new StringBuilder(v);
        for (int i = 1; i < num; i++) {
            sb.append(v);
        }
        return sb.toString();
    }

}
