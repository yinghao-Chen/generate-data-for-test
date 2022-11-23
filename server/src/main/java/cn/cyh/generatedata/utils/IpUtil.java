package cn.cyh.generatedata.utils;

import java.util.regex.Pattern;

/**
 * @author cyh
 * @date 2022/11/23
 */
public class IpUtil {
    private IpUtil(){}

    private static final Pattern IP_REGEX = Pattern.compile(
            "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    public static boolean validIp(String ip) {
        return IP_REGEX.matcher(ip).matches();
    }

}
