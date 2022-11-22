package cn.cyh.generatedata.utils;

import cn.cyh.generatedata.api.enums.Method;
import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.strategy.Strategy;
import cn.cyh.generatedata.strategy.StrategyFactory;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author cyh
 * @date 2022/11/15
 */
public class GenerateUtil {

    private GenerateUtil() {}

    public static final Random RANDOM = new Random();

    public static void generate(Map.Entry<String, Object> entry, Map<String, Object> result, Map<String, Object> map,
                                boolean[] duResult) {
        if(!StringUtils.hasText(entry.getKey())) {
            return;
        }
        // 解析key
        String[] keys = entry.getKey().split("\\|");
        String field = keys[0], rule = null;
        if(keys.length == 2) {
            rule = keys[1];
        }

        Object value = entry.getValue();

        Strategy strategy = null;
        if(rule == null) {
            strategy = StrategyFactory.getStrategy(Rule.NOT_HAS);
        } else if(Rule.NUM_RANGE.getValue().matcher(rule).matches()) {
            strategy = StrategyFactory.getStrategy(Rule.NUM_RANGE);
        } else if(Rule.NUM.getValue().matcher(rule).matches()) {
            strategy = StrategyFactory.getStrategy(Rule.NUM);
        } else if(Rule.FLOAT.getValue().matcher(rule).matches()) {
            strategy = StrategyFactory.getStrategy(Rule.FLOAT);
        } else if(Rule.ONE_MORE.getValue().matcher(rule).matches()) {
            strategy = StrategyFactory.getStrategy(Rule.ONE_MORE);
        } else if(Rule.REGEXP.getValue().matcher(rule).matches()) {
            strategy = StrategyFactory.getStrategy(Rule.REGEXP);
        }
        result.put(field, strategy != null ? strategy.explain(rule, value, map, duResult) : value);
    }

    public static Set<Integer> getRandomNum(int max, int num) {
        Set<Integer> l = new HashSet<>(max);
        while (l.size() < num) {
            l.add(RANDOM.nextInt(max));
        }
        return l;
    }

    public static int getNumRange(String rule) {
        String[] arr = rule.split("-");
        int min = Integer.parseInt(arr[0]);
        int max = Integer.parseInt(arr[1]);
        return getNumRandom(min, max);
    }

    public static int getNumRandom(int min, int max) {
        if (max == min) {
            return min;
        }
        return RANDOM.nextInt(Math.abs(max-min)) + min + 1;
    }

    public static long getNumRandom(long min, long max) {
        return RANDOM.nextInt((int) (max - min)) + min + 1;
    }

    public static int getNumRandom(int num) {
        return num == 0 ? 0 : RANDOM.nextInt(num);
    }

    public static String getResult(String v, int num) {
        StringBuilder sb = new StringBuilder(v);
        for (int i = 1; i < num; i++) {
            sb.append(v);
        }
        return sb.toString();
    }

    public static String getResultFormMap(String s, Map<String, Object> map) {
        String s1 = s.substring(Method.PATH.getValue().length());
        String[] paths = s1.split(":");
        int i = 0;
        while (i < paths.length) {
            String ss = paths[i];
            Object o = map.get(ss);
            if(o == null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if(entry.getKey().startsWith(ss + "|")) {
                        o = entry.getValue();
                        break;
                    }
                }
                if(o == null) {
                    return s;
                }
            }
            if(o instanceof Map) {
                map = (Map<String, Object>) o;
            } else if(o instanceof String) {
                return (String) o;
            } else {
                return s;
            }
            i++;
        }
        return s;
    }

}
