package cn.cyh.generatedata.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 向数据库生成数据
 * @date 2022/11/22
 */
@Data
public class GenToDataSourceVO implements Serializable {

    /** 数据库地址
     * jdbc:mysql://127.0.0.1:3306/abc
     * 192.168.0.1
     **/
    private String url;
    /** 数据库username **/
    private String username;
    /** 数据库password **/
    private String password;

    /**生成数据数量**/
    private Integer count;
    /** 数据库 表/索引/topic/队列 **/
    private String table;
    /** 索引类型 **/
    private String type;

}
