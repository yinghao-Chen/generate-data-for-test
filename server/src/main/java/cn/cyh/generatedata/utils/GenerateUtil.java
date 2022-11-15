package cn.cyh.generatedata.utils;

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

    public static void generate(Map.Entry<String, Object> entry, Map<String, Object> result) {
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
        }
        result.put(field, strategy != null ? strategy.explain(null, value) : value);
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
        return RANDOM.nextInt(max) + Math.abs(max-min);
    }

    public static String getResult(String v, int num) {
        StringBuilder sb = new StringBuilder(v);
        for (int i = 1; i < num; i++) {
            sb.append(v);
        }
        return sb.toString();
    }

}
