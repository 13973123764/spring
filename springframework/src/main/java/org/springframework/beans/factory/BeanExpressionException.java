package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:36
 */
public class BeanExpressionException extends FatalBeanException {

    public BeanExpressionException(String msg) {
        super(msg);
    }

    public BeanExpressionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
