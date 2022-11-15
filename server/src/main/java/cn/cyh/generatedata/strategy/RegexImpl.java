package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.rereg.exception.RegexpIllegalException;
import cn.cyh.generatedata.rereg.exception.TypeNotMatchException;
import cn.cyh.generatedata.rereg.exception.UninitializedException;
import cn.cyh.generatedata.rereg.model.OrdinaryNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@Slf4j
@RuleFunc(rule = Rule.ONE_MORE)
@Component
public class RegexImpl extends Strategy {

    @Override
    protected String explainStr(String rule, Object value) {
        String v = (String) value;
        if(!StringUtils.hasText(v)) {
            return null;
        }
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
