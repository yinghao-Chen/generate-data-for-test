package cn.cyh.generatedata.rereg.model;

import cn.cyh.generatedata.rereg.exception.RegexpIllegalException;
import cn.cyh.generatedata.rereg.exception.TypeNotMatchException;
import cn.cyh.generatedata.rereg.exception.UninitializedException;

public interface Node {

    String getExpression();

    String random() throws UninitializedException, RegexpIllegalException;

    boolean test();

    void init() throws RegexpIllegalException, TypeNotMatchException;

    boolean isInitialized();
}
