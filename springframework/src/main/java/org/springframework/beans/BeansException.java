package org.springframework.beans;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

/**
 * Abstract superclass for all exceptions thrown in the beans package
 * and subpackages
 * 在beans包和子包 为所有异常抛出的抽象超类
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午12:13
 */
public class BeansException extends NestedRuntimeException {

    /**
     * Create a new BeansException with the specified message.
     * 用指定消息创建一个新的 beanException
     * @param msg the detail message  描述信息
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * 用指定消息创建一个新的 beanException 和 根本原因
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
