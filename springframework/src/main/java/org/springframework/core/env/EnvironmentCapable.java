package org.springframework.core.env;

/**
 * Interface indicating a component that contains and exposes an {@link Environment} references.
 * 表名包含和显示一个环境引用 的组件接口
 *
 * All Spring application contexts are EnvironmentCapable, and the interface is used primarily
 * 所有 spring应用上下文 都支持环境, 如果它确实是有效的，该接口主要在框架方法中执行检查，
 * for performing checks in framework methods that accept BeanFactory
 * instances that may or may not actually be applicationContext instances in order to interact
 * with the environment if indeed it is available.
 */
public interface EnvironmentCapable {

    /**
     * Return the {@link Environment} associated with this component.
     * 返回关联 环境的组件
     * @return
     */
    Environment getEnvironment();
}
