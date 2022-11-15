package cn.cyh.generatedata.api.enums;

import java.util.regex.Pattern;

/**
 * @author cyh
 * @date 2022/11/14
 */
public enum Rule {
    /**
     * 定义支持的规则
     */
    NUM_RANGE("/\\d-\\d/"),
    NUM("/\\d/"),
    ONE_MORE("/\\+\\d/"),
    FLOAT("/\\d-\\d.\\d-\\d/"),
    REGEXP("/reg/");

    private Pattern value;
    Rule(String value) {
        this.value = Pattern.compile(value);
    }

    public Pattern getValue() {
        return value;
    }

}
