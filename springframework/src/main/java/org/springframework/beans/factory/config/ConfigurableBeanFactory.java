package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午12:05
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry{


    void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

    @Nullable
    ClassLoader getBeanClassLoader();

    /**
     * Specify the resolution strategy for expressions in bean definition values.
     * 为在bean定义值得表达式 特定的检测策略
     * @param resolver
     */
    void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

    @Nullable
    ConversionService getConversionService();

    void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

    @Nullable
    BeanExpressionResolver getBeanExpressionResolver();

    @Nullable
    String resolveEmbeddedValue(String value);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

    int getBeanPostProcessorCount();

}
