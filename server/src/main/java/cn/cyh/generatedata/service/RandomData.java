package cn.cyh.generatedata.service;

import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/14
 */
public interface RandomData {

    Map<String, Object> generate(Map<String, Object> map);
}
