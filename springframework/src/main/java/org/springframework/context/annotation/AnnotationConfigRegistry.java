package org.springframework.context.annotation;

/**
 * common interface for annotation config application contexts.
 * 为注解配置应用上下文的通用接口
 */
public interface AnnotationConfigRegistry {

    /**
     * register one or more component classes to be processed
     * 注册一个或多个要被处理的组件类
     * @param componentClasses
     */
    void register(Class<?>... componentClasses);

    /**
     * perform a scan within the specified base packages.
     * 对指定基本包 执行扫描
     * @param basePackage
     */
    void scan(String... basePackage);

}
