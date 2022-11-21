package cn.cyh.generatedata.init;

import cn.cyh.generatedata.api.cache.DataCache;
import cn.cyh.generatedata.api.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author cyh
 * @date 2022/11/15
 */
@Slf4j
@Component
public class InitCountryData implements CommandLineRunner, DisposableBean {

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource res1 = new ClassPathResource("data/country_zh.txt");
        try(InputStream inputStream = res1.getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader,512)) {
            StringBuilder sb = new StringBuilder();
            int value;
            while ((value = br.read()) != -1) {
                sb.append((char)value);
            }
            String[] lines = sb.toString().split(Constant.LINE_SPLIT);
            DataCache.COUNTRY_LIST.addAll(Arrays.stream(lines).map(String::trim).collect(Collectors.toList()));
        }catch (IOException e) {
            log.error("InitCountryData 1 error:{}", e.getMessage());
        }

        ClassPathResource res2 = new ClassPathResource("data/country_en.txt");
        try(InputStream inputStream = res2.getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader,512)) {
            StringBuilder sb = new StringBuilder();
            int value;
            while ((value = br.read()) != -1) {
                sb.append((char)value);
            }
            String[] lines = sb.toString().split(Constant.LINE_SPLIT);
            DataCache.COUNTRY_EN_LIST.addAll(Arrays.stream(lines).map(String::trim).collect(Collectors.toList()));
        }catch (IOException e) {
            log.error("InitCountryData 2 error:{}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        DataCache.COUNTRY_LIST.clear();
        DataCache.COUNTRY_EN_LIST.clear();
    }
}
