package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.utils.GenerateUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@RuleFunc(rule = Rule.FLOAT)
@Component
public class FloatImpl extends Strategy {

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map) {
        if(v.startsWith("@")) {

        } else {
            String[] ol = rule.split("\\.");
            return (ol[0].contains("-") ? GenerateUtil.getResult(v, GenerateUtil.getNumRange(ol[0])) : ol[0])
                    + "." + (ol[1].contains("-") ? GenerateUtil.getResult(v, GenerateUtil.getNumRange(ol[1])) : ol[1]);
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
