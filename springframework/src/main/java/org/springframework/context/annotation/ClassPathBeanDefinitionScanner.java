package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * A bean definition scanner that detects bean candidates on the classpath
 * registering corresponding bean definitions with a given registry
 *  检查类路径下可以符合生成bean的bean定义 的扫描器
 *  向给定的注册表中注册相应的bean指令
 *
 *  Candidate classes are detected through configurable type filters.
 *  The default filters include classes that are annotated with Spring's
 * {@link org.springframework.stereotype.Component @Component},
 * {@link org.springframework.stereotype.Repository @Repository},
 * {@link org.springframework.stereotype.Service @Service}, or
 * {@link org.springframework.stereotype.Controller @Controller} stereotype.
 * 候选类可通过配置类型过滤器检测
 * 默认的过滤器包含的类  注解包含以上四种
 *
 * @author ZhouFan
 * @Date 2020/03/28 下午6:17
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private final BeanDefinitionRegistry registry;

    @Nullable
    private Environment environment;

    @Nullable
    private ConditionEvaluator conditionEvaluator;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this(registry, true);
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        this(registry, useDefaultFilters, getOrCreateEnvironment(registry));
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                          Environment environment) {

        this(registry, useDefaultFilters, environment,
                (registry instanceof ResourceLoader ? (ResourceLoader) registry : null));
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                          Environment environment, @Nullable ResourceLoader resourceLoader) {

        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        this.registry = registry;

        if (useDefaultFilters) {
            registerDefaultFilters();
        }
        setEnvironment(environment);
        setResourceLoader(resourceLoader);
    }


    /**
     * Set the Environment to use when resolving placeholders and evaluating
     * {@link Conditional @Conditional}-annotated component classes.
     * <p>The default is a {@link StandardEnvironment}.
     * @param environment the Environment to use
     */
    public void setEnvironment(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        this.environment = environment;
        this.conditionEvaluator = null;
    }

    private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry instanceof EnvironmentCapable) {
            return ((EnvironmentCapable) registry).getEnvironment();
        }
        return new StandardEnvironment();
    }

}
