package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Factory hook that allows for custom modification of an application context's
 * bean definitions, adapting the bean property values of the context's underlying
 * bean factory.
 * 工厂钩子， 允许自动以应用程序上下文bean定义
 * 以及调整上下文底层bean工厂的 bean属性值
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午2:41
 */
public interface BeanFactoryPostProcessor {

    /**
     * Modify the application context's internally bean factory after it's
     * standard initialization. All bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     * 在该对象标准初始化修改应用上下文内部bean工厂， 所有bean定义将会被加载
     * 但是还没有bean被初始化, 这里允许重写或添加属性事件  去初始化bean
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
















}
