package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.utils.GenerateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cyh
 * @date 2022/11/15
 */
@RuleFunc(rule = Rule.NUM)
@Component
public class NumImpl extends Strategy {

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map) {
        if(v.startsWith("@")) {

        } else {
            return GenerateUtil.getResult(v, Integer.parseInt(rule));
        }
        return v;
    }

    @Override
    protected Map<String, Object> explainMap(String rule, Object value) {
        Map<String, Object> v = (Map<String, Object>) value;
        int num = Integer.parseInt(rule);
        if(num >= v.size()) {
            return v;
        } else {
            Set<Integer> randomNum = GenerateUtil.getRandomNum(v.size(), num);
            Map<String, Object> result1 = new HashMap<>(v.size() * 4 / 3 + 1);
            int i = 0;
            for (Map.Entry<String, Object> en : v.entrySet()) {
                if(randomNum.contains(i)) {
                    result1.put(en.getKey(), en.getValue());
                }
                i++;
            }
            return result1;
        }
    }

    @Override
    protected List explainArray(String rule, Object value) {
        List<String> v = (List<String>) value;
        int num = Integer.parseInt(rule);
        if(num >= v.size()) {
            return v;
        } else {
            Set<Integer> randomNum = GenerateUtil.getRandomNum(v.size(), num);
            List<String> l = new ArrayList<>(v.size());
            for (Integer i : randomNum) {
                l.add(v.get(i));
            }
            return l;
        }
    }

    @Override
    protected Boolean explainBoolean(String rule, Object value) {
        Boolean v = (Boolean) value;
        int n = Integer.parseInt(rule);
        return n % 2 == 0 ? v : !v;
    }
}
