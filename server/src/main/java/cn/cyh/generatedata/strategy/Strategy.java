package cn.cyh.generatedata.strategy;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
public abstract class Strategy {

    public Object explain(String rule, Object value, Map<String, Object> map, boolean[] duResult) {
        if(value instanceof String) {
            return explainStrBlank(rule, value, map, duResult);
        } else if (value instanceof Integer || value instanceof Long
                || value instanceof Double || value instanceof Float) {
            return explainStrBlank(rule, String.valueOf(value), map, duResult);
        } else if(value instanceof Map) {
            return explainMap(rule, value);
        } else if (value instanceof List) {
            return explainArray(rule, value);
        } else if (value instanceof Boolean) {
            return explainBoolean(rule, value);
        }
        return value;
    }

    private Object explainStrBlank(String rule, Object value, Map<String, Object> map, boolean[] duResult) {
        String v = (String) value;
        if(!StringUtils.hasText(v)) {
            return null;
        }
        return explainStr(rule, v, map, duResult);
    }

    /**
     * 字符串解析
     * @param rule 规则
     * @param value 值
     * @return v
     */
    protected abstract Object explainStr(String rule, String value, Map<String, Object> map, boolean[] duResult);

    /**
     * 键值对解析
     * @param rule 规则
     * @param value 值
     * @return v
     */
    protected abstract Map<String, Object> explainMap(String rule, Object value);

    /**
     * 数组解析
     * @param rule 规则
     * @param value 值
     * @return v
     */
    protected abstract List explainArray(String rule, Object value);

    /**
     * 布尔类型解析
     * @param rule 规则
     * @param value 值
     * @return v
     */
    protected abstract Boolean explainBoolean(String rule, Object value);
}
