package cn.cyh.generatedata.service.impl;

import cn.cyh.generatedata.api.cache.DataCache;
import cn.cyh.generatedata.api.enums.Method;
import cn.cyh.generatedata.api.enums.Rule;
import cn.cyh.generatedata.api.enums.RuleFunc;
import cn.cyh.generatedata.service.Strategy;
import cn.cyh.generatedata.utils.GenerateUtil;
import cn.cyh.generatedata.utils.RandomStrUtil;
import cn.cyh.generatedata.utils.SfzhUtil;
import com.github.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author DELL
 * @date 2022/11/15
 */
@Slf4j
@RuleFunc(rule = Rule.NOT_HAS)
@Component
public class NotHasImpl extends Strategy {

    private static final String[] CNAME = new String[]{"com", "cn", "org", "top", "net", "cc", "co", "vip", "tech"};
    private static final String[] SEX = new String[]{"男", "女"};

    @Resource
    private Generator atomicGenerator;

    @Override
    protected Object explainStr(String rule, String v, Map<String, Object> map, boolean[] duResult) {
        if(v.startsWith("@")) {
            if(Method.SFZH.getValue().equals(v)) {
                return SfzhUtil.generateSfzh();
            } else if(v.contains(Method.PATH.getValue())) {
                String[] arr = v.split(" ");
                StringJoiner sj = new StringJoiner(" ");
                for (String s : arr) {
                    if(!s.startsWith(Method.PATH.getValue())) {
                        sj.add(s);
                    } else {
                        String formMap = GenerateUtil.getResultFormMap(s, map);
                        if(formMap.startsWith("@")) {
                            duResult[0] = true;
                            sj.add(s);
                        } else {
                            sj.add(formMap);
                        }
                    }
                }
                return sj.toString();
            } else if(Method.STRING.getValue().equals(v)) {
                return RandomStrUtil.getRandomStr(5);
            } else if(v.startsWith(Method.INT.getValue())) {
                if(Method.INT.getValue().equals(v)) {
                    return GenerateUtil.getNumRandom(0, Integer.MAX_VALUE);
                } else {
                    String range = v.substring(Method.INT.getValue().length() + 1, v.length() - 1);
                    String[] nums = range.split(",");
                    if(nums.length != 2) {
                        return v;
                    } else {
                        return GenerateUtil.getNumRandom(Integer.parseInt(nums[0].trim()), Integer.parseInt(nums[1].trim()));
                    }
                }
            } else if(v.startsWith(Method.FLOAT.getValue())) {
                if(Method.FLOAT.getValue().equals(v)) {
                    return GenerateUtil.RANDOM.nextFloat();
                } else {
                    String range = v.substring(Method.FLOAT.getValue().length() + 1, v.length() - 1);
                    String[] nums = range.split(",");
                    if(nums.length < 2 || nums.length > 4) {
                        return v;
                    } else if(nums.length == 2) {
                        int zs = GenerateUtil.getNumRandom(0, Integer.parseInt(nums[0].trim()));
                        int xs = GenerateUtil.getNumRandom(0, Integer.parseInt(nums[1].trim()));
                        return Float.parseFloat(zs + "." + xs);
                    } else if(nums.length == 3) {
                        int zs = GenerateUtil.getNumRandom(Integer.parseInt(nums[0].trim()), Integer.parseInt(nums[1].trim()));
                        int xs = GenerateUtil.getNumRandom(0, Integer.parseInt(nums[2].trim()));
                        return Float.parseFloat(zs + "." + xs);
                    } else {
                        int zs = GenerateUtil.getNumRandom(Integer.parseInt(nums[0].trim()), Integer.parseInt(nums[1].trim()));
                        int xs = GenerateUtil.getNumRandom(Integer.parseInt(nums[2].trim()), Integer.parseInt(nums[3].trim()));
                        return Float.parseFloat(zs + "." + xs);
                    }
                }
            } else if(Method.BOOLEAN.getValue().equals(v)) {
                return GenerateUtil.RANDOM.nextBoolean();
            } else if(v.startsWith(Method.DATETIME.getValue())) {
                long l = LocalDate.of(1970, 1, 1).toEpochDay();
                long l1 = LocalDate.now().toEpochDay();
                long day = GenerateUtil.getNumRandom(l, l1);
                LocalDate date = LocalDate.ofEpochDay(day);
                LocalTime time = LocalTime.of(GenerateUtil.getNumRandom(23), GenerateUtil.getNumRandom(59),
                        GenerateUtil.getNumRandom(59));
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                if(Method.DATETIME.getValue().equals(v)) {
                    return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } else {
                    String format = v.substring(Method.DATETIME.getValue().length() + 1, v.length() - 1);
                    return dateTime.format(DateTimeFormatter.ofPattern(format.trim()));
                }
            } else if(v.startsWith(Method.DATE.getValue())) {
                long l = LocalDate.of(1970, 1, 1).toEpochDay();
                long l1 = LocalDate.now().toEpochDay();
                long day = GenerateUtil.getNumRandom(l, l1);
                if(Method.DATE.getValue().equals(v)) {
                    return LocalDate.ofEpochDay(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else {
                    String format = v.substring(Method.DATE.getValue().length() + 1, v.length() - 1);
                    return LocalDate.ofEpochDay(day).format(DateTimeFormatter.ofPattern(format.trim()));
                }
            } else if(Method.DATE_NOW.getValue().equals(v)) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if(Method.URL.getValue().equals(v)) {
                StringBuilder sb = new StringBuilder();
                int i = GenerateUtil.getNumRandom(6);
                if((i & 1) == 0) {
                    sb.append("http://192.168.");
                    sb.append(GenerateUtil.getNumRandom(255));
                    sb.append(".");
                    sb.append(GenerateUtil.getNumRandom(255));
                    sb.append(":");
                    sb.append(GenerateUtil.getNumRandom(80, 20000));
                    sb.append("/");
                } else {
                    sb.append("https://");
                    for (int i1 = 0; i1 < 2; i1++) {
                        sb.append(RandomStrUtil.getRandomStr(i)).append(".");
                    }
                    sb.append(CNAME[GenerateUtil.getNumRandom(CNAME.length)]);
                    sb.append("/");
                }
                for (int i1 = 0; i1 < i; i1++) {
                    sb.append(RandomStrUtil.getRandomStr(i)).append("/");
                }
                return sb.toString();
            } else if(Method.EMAIL.getValue().equals(v)) {
                StringBuilder sb = new StringBuilder();
                int i = GenerateUtil.getNumRandom(3);
                for (int i1 = 0; i1 < i; i1++) {
                    sb.append(RandomStrUtil.getRandomStr(GenerateUtil.getNumRandom(8)));
                }
                sb.append("@").append(RandomStrUtil.getRandomStr(3)).append(".com");
                return sb.toString();
            } else if(Method.SEX.getValue().equals(v)) {
                return SEX[GenerateUtil.getNumRandom(10) & 1];
            } else if(Method.CITY.getValue().equals(v)) {
                return DataCache.CITY_LIST.get(GenerateUtil.getNumRandom(DataCache.CITY_LIST.size()));
            } else if(Method.PROVINCE.getValue().equals(v)) {
                return DataCache.PROVINCE_LIST.get(GenerateUtil.getNumRandom(DataCache.PROVINCE_LIST.size()));
            } else if(Method.COUNTRY.getValue().equals(v)) {
                return DataCache.COUNTRY_LIST.get(GenerateUtil.getNumRandom(DataCache.PROVINCE_LIST.size()));
            } else if(Method.COUNTRY_EN.getValue().equals(v)) {
                return DataCache.COUNTRY_EN_LIST.get(GenerateUtil.getNumRandom(DataCache.COUNTRY_EN_LIST.size()));
            } else if(Method.ID.getValue().equals(v)) {
                return atomicGenerator.nextId();
            } else if(Method.UUID.getValue().equals(v)) {
                return UUID.randomUUID().toString().replace("-", "");
            }
        }
        return v;
    }

    @Override
    protected Map<String, Object> explainMap(String rule, Object value) {
        // 处理内嵌函数
        boolean[] duResult = new boolean[1];

        Map<String, Object> v = (Map<String, Object>) value;
        Map<String, Object> result1 = new LinkedHashMap<>(v.size() * 4 / 3 + 1);
        for (Map.Entry<String, Object> o : v.entrySet()) {
            GenerateUtil.generate(o, result1, v, duResult);
        }
        while (duResult[0]) {
            duResult[0] = false;
            for (Map.Entry<String, Object> entry : result1.entrySet()) {
                GenerateUtil.generate(entry, result1, result1, duResult);
            }
        }
        return result1;
    }

    @Override
    protected List explainArray(String rule, Object value) {
        return (List) value;
    }

    @Override
    protected Boolean explainBoolean(String rule, Object value) {
        return (Boolean) value;
    }
}
