package cn.cyh.generatedata.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 向数据库生成数据
 * @date 2022/11/22
 */
@Data
public class GenToDataSourceSaveVO implements Serializable {

    private GenToDataSourceVO meta;

    private Map<String, Object> ruleData;

}
