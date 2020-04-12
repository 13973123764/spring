package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.lang.Nullable;

/**
 * A root bean definition represents the merged bean definition that backs
 * a specific bean in a Spring BeanFacotory at runtime.
 * 根bean定义表示在运行时支持springbeanfacoory中特定bean的合并bean定义。
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午3:14
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    @Nullable
    private volatile Object beanClass;


    RootBeanDefinition(BeanDefinition original){
        super(original);
    }

    /**
     * Create a new RootBeanDefinition for a singleton.
     * 为单例创建一个新的 根bean定义
     * @param beanClass the class of the bean to instantiate
     */
    public RootBeanDefinition(@Nullable Class<?> beanClass) {
        super();
        setBeanClass(beanClass);
    }

    /**
     * Specify the class for this bean.
     */
    public void setBeanClass(@Nullable Class<?> beanClass) {
        this.beanClass = beanClass;
    }


}
