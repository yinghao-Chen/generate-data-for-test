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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author cyh
 * @date 2022/11/14
 */
@Slf4j
@Service
public class RandomDataImpl implements RandomData {

    private static final Random RANDOM = new Random();

    @Override
    public Map<String, Object> generate(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>(map.size() * 4 / 3 + 1);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            explain(entry, result);
        }
        return result;
    }

    private void explain(Map.Entry<String, Object> entry, Map<String, Object> result) {
        if(!StringUtils.hasText(entry.getKey())) {
            return;
        }
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
            if(!StringUtils.hasText(v)) {
                result.put(field, null);
            } else if(v.startsWith("@")) {
                if(Method.SFZH.getValue().equals(v)) {
                    // "sfzh": "@sfzh"
                    result.put(field, SfzhUtil.generateSfzh());
                } else if(v.contains(Method.PATH.getValue())) {
                    // explain like "absolutePath": "@/string @/user/name"
                    String[] arr = v.split(" ");
                    StringJoiner sj = new StringJoiner(" ");
                    for (String s : arr) {
                        if(!s.startsWith(Method.PATH.getValue())) {
                            sj.add(s);
                        } else {
                            String[] paths = s.substring(Method.PATH.getValue().length()).split("/");
                            // TODO 最后解析
                        }
                    }
                }
            } else {
                // 普通字符串
                if(rule == null) {
                    result.put(field, v);
                } else if(Rule.NUM_RANGE.getValue().matcher(rule).matches()) {
                    // "string|1-10": "★"
                    result.put(field, getResult(v, getNumRange(rule)));
                } else if(Rule.NUM.getValue().matcher(rule).matches()) {
                    // "string2|3": "★★"
                    result.put(field, getResult(v, Integer.parseInt(rule)));
                } else if(Rule.ONE_MORE.getValue().matcher(rule).matches()) {
                    // "number|+1": 202
                    result.put(field, getResult(v, RANDOM.nextInt(5)));
                } else if(Rule.FLOAT.getValue().matcher(rule).matches()) {
                    // "number2|1-100.1-10": 1
                    String[] ol = rule.split("\\.");
                    result.put(field, (ol[0].contains("-") ? getResult(v, getNumRange(ol[0])) : ol[0])
                            + "." + (ol[1].contains("-") ? getResult(v, getNumRange(ol[1])) : ol[1]));
                } else if(Rule.REGEXP.getValue().matcher(rule).matches()) {
                    // "regexp|reg": /[a-z][A-Z][0-9]/
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
                // "boolean|1": true
                int n = Integer.parseInt(rule);
                result.put(field, n % 2 == 0 ? v : !v);
            }
        } else if(value instanceof Map) {
            Map<String, Object> v = (Map<String, Object>) value;
            // "user": {"name": "demo"}
            if (rule == null) {
                Map<String, Object> result1 = new LinkedHashMap<>(v.size() * 4 / 3 + 1);
                for (Map.Entry<String, Object> o : v.entrySet()) {
                    explain(o, result1);
                }
                result.put(field, result1);
            } else if (Rule.NUM.getValue().matcher(rule).matches()) {
                // "object|2": {"310000": "上海市", "320000": "江苏省"}  选取num个
                int num = Integer.parseInt(rule);
                if(num >= v.size()) {
                    result.put(field, v);
                } else {
                    Set<Integer> randomNum = getRandomNum(v.size(), num);
                    Map<String, Object> result1 = new HashMap<>(v.size() * 4 / 3 + 1);
                    int i = 0;
                    for (Map.Entry<String, Object> en : v.entrySet()) {
                        if(randomNum.contains(i)) {
                            result1.put(en.getKey(), en.getValue());
                        }
                        i++;
                    }
                    result.put(field, result1);
                }
            }
        } else if (value instanceof List) {
            List<String> v = (List<String>) value;
            if(rule == null) {
                result.put(field, value);
            } else if(Rule.NUM.getValue().matcher(rule).matches()) {
                // "array|1": [ "AMD" ]
                int num = Integer.parseInt(rule);
                if(num >= v.size()) {
                    result.put(field, v);
                } else {
                    Set<Integer> randomNum = getRandomNum(v.size(), num);
                    List<String> l = new ArrayList<>(v.size());
                    for (Integer i : randomNum) {
                        l.add(v.get(i));
                    }
                    result.put(field, l);
                }
            }
        }
        if(!result.containsKey(field)) {
            result.put(field, value);
        }
    }

    private Set<Integer> getRandomNum(int max, int num) {
        Set<Integer> l = new HashSet<>(max);
        while (l.size() < num) {
            l.add(RANDOM.nextInt(max));
        }
        return l;
    }

    private int getNumRange(String rule) {
        String[] arr = rule.split("-");
        int min = Integer.parseInt(arr[0]);
        int max = Integer.parseInt(arr[1]);
        return RANDOM.nextInt(max) + Math.abs(max-min);
    }

    private String getResult(String v, int num) {
        StringBuilder sb = new StringBuilder(v);
        for (int i = 1; i < num; i++) {
            sb.append(v);
        }
        return sb.toString();
    }

}
