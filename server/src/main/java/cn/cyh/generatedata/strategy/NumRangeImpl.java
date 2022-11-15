package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Method;
import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.utils.GenerateUtil;
import cn.cyh.generatedata.utils.RandomStrUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@RuleFunc(rule = Rule.NUM_RANGE)
@Component
public class NumRangeImpl extends Strategy {

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map) {
        if(v.startsWith("@")) {
            if(Method.STRING.getValue().equals(v)) {
                final int strLen = 5;
                String str = RandomStrUtil.getRandomStr(strLen);
                StringBuilder sb = new StringBuilder(str);
                for (int i = 1; i < GenerateUtil.getNumRange(rule); i++) {
                    sb.append(RandomStrUtil.getRandomStr(strLen));
                }
                return sb.toString();
            }
        } else {
            return GenerateUtil.getResult(v, GenerateUtil.getNumRange(rule));
        }
        return v;
    }

    @Override
    protected Map<String, Object> explainMap(String rule, Object value) {
        return null;
    }

    @Override
    protected List explainArray(String rule, Object value) {
        return null;
    }

    @Override
    protected Boolean explainBoolean(String rule, Object value) {
        return null;
    }
}
