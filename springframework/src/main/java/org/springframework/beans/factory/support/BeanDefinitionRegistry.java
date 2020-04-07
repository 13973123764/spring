package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

public interface BeanDefinitionRegistry extends AliasRegistry {

    /**
     * Check if this registry contains a bean definition with the given name.
     * 从给定的bean名称 检查注册器是否包含该bean定义
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Register a new bean definition with this registry.
     * Must support RootBeanDefinition and ChildBeanDefinition.
     * @param beanName the name of the bean instance to register
     * @param beanDefinition definition of the bean instance to register
     * @throws BeanDefinitionStoreException if the BeanDefinition is invalid
     * @throws BeanDefinitionOverrideException if there is already a BeanDefinition
     * for the specified bean name and we are not allowed to override it
     * @see GenericBeanDefinition
     * @see RootBeanDefinition
     * @see ChildBeanDefinition
     *
     * 在当前注册器中注册一个新的bean定义对象
     * 必须支持 RootBeanDefinition and ChildBeanDefinition.
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    /**
     * Remove the BeanDefinition for the given name.
     * 从给定名称中删除一个bean定义
     * @param beanName 要去查找的bean定义名称
     * @throws NoSuchBeanDefinitionException   如果没找到bean定义 则抛出异常
     */
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefnition(String beanName) throws NoSuchBeanDefinitionException;

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    boolean isBeanNameInUse(String beanName);
}
