package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * Extension to the standard {@link BeanFactoryPostProcessor} SPI, allowing for
 * the registration of further bean definitions <i>before</i> regular
 * BeanFactoryPostProcessor detection kicks in. In particular,
 * BeanDefinitionRegistryPostProcessor may register further bean definitions
 * which in turn define BeanFactoryPostProcessor instances.
 *
 * 拓展标准 BeanFactoryPostProcessor 的对象
 * 允许去注册更多的bean定义对象 常规的 BeanFactoryPostProcessor 检测启动
 * BeanFactoryPostProcessor 可以注册跟多的bean定义, 这些对象返回定义 BeanFactoryPostProcessor 的实例
 * @author ZhouFan
 * @Date 2020/03/29 下午2:34
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {



}
