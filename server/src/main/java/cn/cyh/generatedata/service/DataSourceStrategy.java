package cn.cyh.generatedata.service;

import cn.cyh.generatedata.api.vo.GenToDataSourceSaveVO;
import cn.cyh.generatedata.api.vo.GenToDataSourceVO;

/**
 * @author cyh
 * @date 2022/11/22
 */
public interface DataSourceStrategy {

    /**
     * 测试连接
     * @param vo 连接信息
     **/
    boolean test(GenToDataSourceVO vo);

    /**
     * 生成数据
     * @param vo 连接信息+规则信息
     */
    int saveData(GenToDataSourceSaveVO vo);

}
