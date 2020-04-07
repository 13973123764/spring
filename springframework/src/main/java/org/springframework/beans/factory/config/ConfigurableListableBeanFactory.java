package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.lang.Nullable;

/**
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午12:12
 */
public interface ConfigurableListableBeanFactory
        extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory{

    void ignoreDependencyInterface(Class<?> ifc);

    void registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue);

}
