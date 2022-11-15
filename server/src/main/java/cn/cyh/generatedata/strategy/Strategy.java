package cn.cyh.generatedata.strategy;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
public abstract class Strategy {

    public Object explain(String rule, Object value) {
        if(value instanceof String) {
            explainStr(rule, value);
        }else if(value instanceof Map) {
            explainMap(rule, value);
        } else if (value instanceof List) {
            explainArray(rule, value);
        } else if (value instanceof Boolean) {
            explainBoolean(rule, value);
        }
        return value;
    }

    /**
     * 字符串解析
     * @param rule 规则
     * @param value 值
     */
    protected abstract String explainStr(String rule, Object value);

    /**
     * 键值对解析
     * @param rule 规则
     * @param value 值
     */
    protected abstract Map<String, Object> explainMap(String rule, Object value);

    /**
     * 数组解析
     * @param rule 规则
     * @param value 值
     */
    protected abstract List explainArray(String rule, Object value);

    /**
     * 布尔类型解析
     * @param rule 规则
     * @param value 值
     */
    protected abstract Boolean explainBoolean(String rule, Object value);
}
