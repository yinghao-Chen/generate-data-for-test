package cn.cyh.generatedata.api.enums;

/**
 * @author cyh
 * @date 2022/11/14
 */
public enum Method {
    /**
     * 定义支持的函数
     */
    SFZH("@sfzh");

    private String value;
    Method(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
