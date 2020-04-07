package org.springframework.beans.factory;


import org.springframework.beans.BeansException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午9:29
 */
@FunctionalInterface
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
