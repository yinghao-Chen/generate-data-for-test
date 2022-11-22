package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.api.enums.RuleFunc;
import cn.cyh.generatedata.service.Strategy;
import cn.cyh.generatedata.utils.GenerateUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@RuleFunc(rule = Rule.ONE_MORE)
@Component
public class OneMoreImpl extends Strategy {

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map, boolean[] duResult) {
        if(v.startsWith("@")) {

        } else {
            return GenerateUtil.getResult(v, GenerateUtil.RANDOM.nextInt(5));
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
