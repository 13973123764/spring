package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be
 * reloaded if the implementation supports this.
 * 为应用程序提供配置的中心接口，应用程序运行时 此对象只读，
 * 如果实现支持此接口可能会重新加载
 *
 * An applicationContext provides:
 * 一个应用上下文支持以下:
 * 1.Bean factory methods for accessing application components.
 * Inherited from {@link org.springframework.beans.factory.ListableBeanFactory}.
 * 用于访问应用组件 bean工厂方法。 继承来自ListableBeanFactory
 * 2.The ability to load file resources in a generic fashion.
 * Inherited from the {@link org.springframework.core.io.ResourceLoader} interface.
 * 以通用的方式加载文件资源的能力. 继承来自 ResourceLoader 接口
 * 3.The ability to resolve messages, supporting internationalization.
 * Inherited from the {@link MessageSource} interface.
 * 解析消息的能力，并且支持国际化 继承来自 MessageSource
 * 4.Inheritance from a parent context. Definitions in a descendant context
 * will always take priority. This means, for example, that a single parent
 * context can be used by an entire web application, while each servlet has
 * its own child context that is independent of that of any other servlet.
 * 继承于父级上下文, 子级上下文的定义始终优先, 一个单例父容器可以被入口web应用所使用，
 * 每一个servlet 都有它自身的子上下文 且独立于任何其他servlet
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
                                        MessageSource, ApplicationEventPublisher, ResourcePatternResolver {


    /**
     * return the unique id of this application context.
     * 返回应用上下文唯一id
     * @return
     */
    @Nullable
    String getId();

    /**
     * return a name for the deployed application that this context belongs to.
     * 返回此上下文所属的 已部署的应用名称
     * @return
     */
    String getApplicationName();

    /**
     * Return a friendly name for this context.
     * 返回一个友好的名称
     * @return
     */
    String getDisplayName();

    /**
     * return the timestamp when this context was first loaded
     * 当上下文第一次加载后返回 时间戳
     */
    long getStartupDate();

    /**
     * Return the parent context, or {@code null} if there is no parent
     * and this is the root of context hierarchy
     * 返回上下文父类 或者 null(如果没有父类且这是一个上下文层级 根节点)
     * @return
     */
    @Nullable
    ApplicationContext getParent();

    /**
     * Expose AutowireCapableBeanFactory functionality for this context.
     * 为当前上下文暴露自动可自动注入的bean工厂功能
     * This is not typically used by application code, except for the purse of
     * 这里通常不被用在应用程序代码里
     * initializing bean instances that live outside of the application context,
     * 除了初始化存活在应用上下文以外的bean实例 作目的
     * applying the Spring bean lifecycle (fully or partly) to them.
     * 还应用了 spring bean生命周期 到它们
     * Alternatively, the internal BeanFactory exposed by the
     *
     * {@link ConfigurableApplicationContext} interface offers access to the
     * {@link AutowireCapableBeanFactory} interface too. The present method mainly
     * serves as a convenient, specific facility on the ApplicationContext interface
     *
     * @return
     * @throws IllegalStateException
     */
    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
