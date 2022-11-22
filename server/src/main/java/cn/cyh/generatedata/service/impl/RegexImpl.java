package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.api.enums.RuleFunc;
import cn.cyh.generatedata.rereg.exception.RegexpIllegalException;
import cn.cyh.generatedata.rereg.exception.TypeNotMatchException;
import cn.cyh.generatedata.rereg.exception.UninitializedException;
import cn.cyh.generatedata.rereg.model.OrdinaryNode;
import cn.cyh.generatedata.service.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@Slf4j
@RuleFunc(rule = Rule.REGEXP)
@Component
public class RegexImpl extends Strategy {

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map, boolean[] duResult) {
        try {
            return new OrdinaryNode(v).random();
        } catch (UninitializedException | RegexpIllegalException | TypeNotMatchException e) {
            log.error("RandomData 'RuleFunc.REGEXP generate' Exception happened", e);
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
