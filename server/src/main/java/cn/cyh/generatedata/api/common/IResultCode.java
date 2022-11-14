package cn.cyh.generatedata.api.common;

import java.io.Serializable;

/**
 * @author cyh
 * @date 2022/11/14
 */
public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}
