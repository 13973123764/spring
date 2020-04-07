package org.springframework.context.support;


import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * generic ApplicationContext implementation that holds a single internal
 * {@link org.springframework.beans.factory.support.DefaultListableBeanFactory}
 * instance and does not assume a specific bean definition format. Implements
 * the {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}
 * interface in order to allow for applying any bean definition readers to it.
 *
 *  持有单个内部的DefaultListableBeanFactory的实例, 并且没有特定bean定义格式 的构建应用上下文实现
 * 实现 BeanDefinitionRegistry 接口是为了允许 应用任何bean定义读取到它
 *
 * Typical usage is to register a variety of bean definitions via the
 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}
 * interface and then call {@link #refresh()} to initialize those beans
 * with application context semantics
 * {@link org.springframework.context.ApplicationContextAware}
 * {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor}
 *
 * 典型的使用是用BeanDefinitionRegistry去注册各种bean定义
 * 然后调用 refresh() 方法去初始化那些beans和应用上下文语法
 *
 * In contrast to other ApplicationContext implementations that create a new
 * internal BeanFactory instance for each refresh, the internal BeanFactory
 * in context is available right from start. to be able to register bean
 * definition on it #{refresh()} may called once.
 *
 *
 *
 *
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    /** 默认list bean工厂 */
    @Nullable
    private final DefaultListableBeanFactory beanFactory;

    @Nullable
    private ResourceLoader resourceLoader;

    /** 自定义加载器 */
    private boolean customClassLoader = false;
    /** 原子类布尔型  是否刷新完成 */
    private final AtomicBoolean refreshed = new AtomicBoolean();

    /**
     * create a new GenericApplicationContext
     * 创建一个默认的应用程序上下文构建
     * @see #registerBeanDefinition     参考方法
     * @see #refresh                    参考方法
     */
    public GenericApplicationContext(){
        this.beanFactory = new DefaultListableBeanFactory();
    }


    /**
     * create new GenericApplicationContext with the given DefaultBeanFactory
     * 从给定的bean工厂中创建一个新的 一般应用程序上下文
     * @param beanFactory 为当前上下文使用的 DefaultListableBeanFactory 实例
     * @see #registerBeanDefinition
     */
    public GenericApplicationContext(DefaultListableBeanFactory beanFactory){
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }


    /**
     * Create a new GenericApplicationContext with the given parent.
     * 当给定父类 时创建一个新的一般应用程序上下文
     * @param parent    父类
     * @see #refresh
     */
    public GenericApplicationContext(@Nullable ApplicationContext parent){
        // 先创建一个默认的 DefaultListableBeanFactory
        this();
        setParent(parent);
    }

    /**
     * Create a new GenericApplicationContext with the given DefaultListableBeanFactory.
     * @param beanFactory the DefaultListableBeanFactory instance to use for this context
     * @param parent the parent application context
     * @see #registerBeanDefinition
     * @see #refresh
     */
    public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent) {
        this(beanFactory);
        setParent(parent);
    }

    /**
     * Set the parent of this application context, also setting
     * the parent of the internal BeanFactory accordingly.
     * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#setParentBeanFactory
     */
    @Override
    public void setParent(@Nullable ApplicationContext parent) {
        super.setParent(parent);
        this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
    }



}
