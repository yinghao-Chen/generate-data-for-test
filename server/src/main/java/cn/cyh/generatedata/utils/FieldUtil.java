package cn.cyh.generatedata.utils;

import java.util.Set;

/**
 * @author cyh
 * @date 2022/11/24
 */
public class FieldUtil {
    private FieldUtil() {}

    public static String existsFields(Set<String> fields, String field) {
        if(fields.contains(field)) {
            return field;
        }
        for (String s : fields) {
            if(s.toUpperCase().equals(field.toUpperCase())) {
                return s;
            }
        }
        return null;
    }
}
