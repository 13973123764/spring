package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;

public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private ConditionEvaluator conditionEvaluator;

    /**
     * Create a new {@code AnnotatedBeanDefinitionReader} for the given registry.
     * 从给定的注解器中 创建一个注解bean定义读取器
     * @param registry
     */
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry){
        this(registry, getOrCreateEnvironment(registry));
    }


    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        Assert.notNull(environment, "Environment must not be null");
        // 赋值bean定义注册器
        this.registry = registry;
        // 创建条件鉴别器
        this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
        // 注册注解配置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
    }


    private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry instanceof EnvironmentCapable) {
            return ((EnvironmentCapable) registry).getEnvironment();
        }
        return new StandardEnvironment();
    }

























}
