package org.springframework.context.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextAware;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午4:49
 */
public class EventListenerMethodProcessor
            implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
