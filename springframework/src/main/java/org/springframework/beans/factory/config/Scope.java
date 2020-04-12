package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:56
 */
public interface Scope {

    Object get(String name, ObjectFactory<?> objectFactory);
}
