package cn.cyh.generatedata.api.enums;

/**
 * @author cyh
 * @date 2022/11/22
 */
public enum DataSource {
    /**
     * 定义支持的数据源
     */
    MYSQL("mysql"),
    ORACLE(""),
    ELASTIC_SEARCH(""),
    KAFKA(""),
    RABBIT_MQ("");

    private String value;
    DataSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
