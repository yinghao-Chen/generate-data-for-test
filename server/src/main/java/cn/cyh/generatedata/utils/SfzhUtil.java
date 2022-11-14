package cn.cyh.generatedata.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author cyh
 * @date 2022/11/11
 */
public class SfzhUtil {

    private SfzhUtil() {}

    /**
     * 身份证前两位对应的身份
     */
    private static final Map<Integer, String> PROVS_MAP = new HashMap<Integer, String>(16) {{
        put(11, "北京");  put(12, "天津");  put(13, "河北");  put(14, "山西");  put(15, "内蒙古");
        put(21, "辽宁");  put(22, "吉林");  put(23, "黑龙江");
        put(31, "上海");  put(32, "江苏");  put(33, "浙江");  put(34, "安徽");  put(35, "福建");  put(36, "江西");  put(37, "山东");
        put(41, "河南");  put(42, "湖北");  put(43, "湖南");  put(44, "广东");  put(45, "广西");  put(46, "海南");
        put(50, "重庆");  put(51, "四川");  put(52, "贵州");  put(53, "云南");  put(54, "西藏");
        put(61, "陕西");  put(62, "甘肃");  put(63, "青海");  put(64, "宁夏");  put(65, "新疆");
        put(71, "台湾");  put(81, "香港");  put(82, "澳门");
    }};
    private static final String[] CHECK_OPTIONS = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "51", "52", "53", "54", "50", "61", "62", "63", "64", "65", "71", "81", "82"};

    /**
     * 身份证前17位因子
     * length=17
     */
    private static final int[] FACTOR = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 第18位与11取模对应值
     */
    private static final String[] PARITY = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static String generateSfzh() {
        Random random = new Random();
        // 省份
        String prov = CHECK_OPTIONS[random.nextInt(CHECK_OPTIONS.length - 1)];
        // 2-4位
        String prefix = String.format("%04d", random.nextInt(9998) + 1);
        // 随机生日
        LocalDate now = LocalDate.now();
        LocalDate date = LocalDate.ofEpochDay(random.nextInt((int) now.toEpochDay()));
        String birth = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 15-17位
        String prefix2 = String.format("%03d", random.nextInt(998) + 1);

        String sfzh17 = prov + prefix + birth + prefix2;
        // 18位
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += sfzh17.charAt(i) * FACTOR[i];
        }
        return sfzh17 + PARITY[sum % 11];
    }

}
