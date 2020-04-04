package org.springframework.context;

import org.springframework.lang.Nullable;

import java.io.Closeable;

/**
 * SPI interface to be implemented by most if not all application contexts.
 * Provides facilities to configure an application context in addition
 * to the application context client methods in the ApplicationContext interface.
 * {@link org.springframework.context.ApplicationContext}
 *
 * SPI接口被大多数(如果不是所有应用程序上下文)实现
 * 在应用程序上下文接口除了 应用程序上下文客户端方法以外还提供了 配置应用上下文的工具
 *
 * Configuration and lifecycle methods are encapsulated here, avoid
 * making them obvious to ApplicationContext client code. The present
 * methods should only be used by startup and shutdown code.
 *
 * 配置和生命周期方法被封装到这，避免应用上下文客户端代码 显而易见， 这里提供
 * 的方法应该只被用于 启动和关闭
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {


    /**
     * asd
     * Any number of these characters are considered delimiters between
     * multiple context config paths in a single String value.
     * 这些字符串任何数量都被看做单个字符串中多个上下文配置路径的分隔符
     */
    String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    /**
     * Name of the ConversionService bean in the factory
     * If none is supplied , default conversion rules apply
     * 在工厂中 ConversionService bean 的名称
     * 如果没有提供，则应用默认conversion规则
     */
    String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

    /**
     * Name of the LoadTimeWeaver bean in the factory. If such a bean is supplied,
     * the context will use a temporary ClassLoader for type matching, in order
     * to allow the LoadTimeWeaver to process all actual bean classes.
     * 在工厂中名称为 LoadTimeWeaver 的bean. 如果提供了这个bean
     * 上下文将使用一个模板类加载器去进行类型匹配，为允许 LoadTimeWeaver 去 处理
     * 所有实际的bean classes
     */
    String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";

    /**
     * Name of the {@link Environment} bean in the factory
     * 工厂中的bean名称
     */
    String ENVIRONMENT_BEAN_NAME = "environment";

    /**
     * Name of the System properties bean in the factory.
     * 工厂中的系统参数 bean名称
     */
    String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

    /**
     * Name of the System environment bean in the factory
     * 工厂中的系统环境bean名称
     */
    String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

    /**
     * Set the unique id of this application context
     * 给当前应用上下文设置唯一id
     */
    void setId();

    /**
     * Set the parent of this application context.
     *
     * @param parent
     */
    void setParent(@Nullable ApplicationContext parent);

}










