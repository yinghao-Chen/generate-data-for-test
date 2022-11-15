package cn.cyh.generatedata.utils;

import java.util.Random;

/**
 * @author cyh
 * @date 2022/11/15
 */
public class RandomStrUtil {

    private RandomStrUtil(){}

    private static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getRandomStr(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(STR.charAt(random.nextInt(62)));
        }
        return sb.toString();
    }
}
