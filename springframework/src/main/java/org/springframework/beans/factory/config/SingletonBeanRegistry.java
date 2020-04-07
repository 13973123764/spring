package org.springframework.beans.factory.config;

import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:18
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    @Nullable
    Object getSingleton(String beanName);
}
