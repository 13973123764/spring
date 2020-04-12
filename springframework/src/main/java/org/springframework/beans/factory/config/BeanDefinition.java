package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    int ROLE_INFRASTRUCTURE = 2;

    int ROLE_APPLICATION = 0;

    @Nullable
    String getBeanClassName();

    void setRole(int role);

    int getRole();

    @Nullable
    String getParentName();

    void setParentName(@Nullable String parentName);

    void setBeanClassName(@Nullable String beanClassName);

    void setLazyInit(boolean lazyInit);

    @Nullable
    String getScope();

    void setScope(@Nullable String scope);

    boolean isAbstract();

    boolean isLazyInit();

    boolean isSingleton();

    @Nullable
    String getFactoryBeanName();

    @Nullable
    String[] getDependsOn();

    void setDependsOn(@Nullable String... dependsOn);

    boolean isAutowireCandidate();

    @Nullable
    String getFactoryMethodName();

    void setFactoryBeanName(@Nullable String factoryBeanName);

    void setFactoryMethodName(@Nullable String factoryMethodName);

    default boolean hasConstructorArgumentValues() {
        return !getConstructorArgumentValues().isEmpty();
    }

    ConstructorArgumentValues getConstructorArgumentValues();

    default boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }

    MutablePropertyValues getPropertyValues();

    void setAutowireCandidate(boolean autowireCandidate);

    boolean isPrimary();

    void setPrimary(boolean primary);

    @Nullable
    String getResourceDescription();

}
