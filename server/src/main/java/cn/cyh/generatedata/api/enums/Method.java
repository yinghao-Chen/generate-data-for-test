package cn.cyh.generatedata.api.enums;

/**
 * @author cyh
 * @date 2022/11/14
 */
public enum Method {
    /**
     * 定义支持的函数
     */
    SFZH("@sfzh"),
    PATH("@/"),
    STRING("@str"),
    INT("@int"),
    FLOAT("@float"),
    BOOLEAN("@boolean"),
    DATE("@date"),
    DATETIME("@datetime"),
    DATE_NOW("@now"),
    URL("@url"),
    EMAIL("@email"),
    SEX("@sex"),
    CITY("@city"),
    PROVINCE("@province"),
    ID("@id"),
    UUID("@uuid");

    private String value;
    Method(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
