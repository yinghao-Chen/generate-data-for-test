package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Method;
import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.utils.GenerateUtil;
import cn.cyh.generatedata.utils.RandomStrUtil;
import cn.cyh.generatedata.utils.SfzhUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author DELL
 * @date 2022/11/15
 */
@RuleFunc(rule = Rule.NOT_HAS)
@Component
public class NotHasImpl extends Strategy {

    @Override
    protected String explainStr(String rule, Object value) {
        String v = (String) value;
        if(!StringUtils.hasText(v)) {
            return null;
        } else if(v.startsWith("@")) {
            if(Method.SFZH.getValue().equals(v)) {
                return SfzhUtil.generateSfzh();
            } else if(v.contains(Method.PATH.getValue())) {
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
            } else if(Method.STRING.getValue().equals(v)) {
                return RandomStrUtil.getRandomStr(5);
            }
        }
        return v;
    }

    @Override
    protected Map<String, Object> explainMap(String rule, Object value) {
        Map<String, Object> v = (Map<String, Object>) value;
        Map<String, Object> result1 = new LinkedHashMap<>(v.size() * 4 / 3 + 1);
        for (Map.Entry<String, Object> o : v.entrySet()) {
            GenerateUtil.generate(o, result1);
        }
        return result1;
    }

    @Override
    protected List explainArray(String rule, Object value) {
        return (List) value;
    }

    @Override
    protected Boolean explainBoolean(String rule, Object value) {
        return (Boolean) value;
    }
}
