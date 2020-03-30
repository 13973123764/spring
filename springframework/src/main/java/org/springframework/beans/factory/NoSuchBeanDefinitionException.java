package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/**
 * Exception thrown when a is asked for a bean instance for which it
 * cannot find a definition. This may point to a non-existing bean, a non-unique bean,
 * or a manually registered singleton instance without an associated bean definition.
 * 当请求一个bean实例却找不到定义, 这种情况可能是 bean不存在, 不是唯一的bean或者 手动注册了没有关联bean定义的一个单实例
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:44
 */
public class NoSuchBeanDefinitionException extends BeansException {


    public NoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
