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
    NOT_HAS(""),
    NUM_RANGE("^[1-9][0-9]{0,}-[1-9][0-9]{0,}$"),
    NUM("^[1-9][0-9]{0,}$"),
    ONE_MORE("^\\+[1-9][0-9]{0,}$"),
    FLOAT("^[1-9][0-9]{0,}-[1-9][0-9]{0,}\\.[1-9][0-9]{0,}-[1-9][0-9]{0,}$"),
    REGEXP("^reg$");

    private Pattern value;
    Rule(String value) {
        this.value = Pattern.compile(value);
    }

    public Pattern getValue() {
        return value;
    }

}
