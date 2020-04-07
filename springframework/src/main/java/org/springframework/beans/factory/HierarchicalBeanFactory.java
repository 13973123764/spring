package org.springframework.beans.factory;

import org.springframework.lang.Nullable;

public interface HierarchicalBeanFactory extends BeanFactory{

    /**
     * Return the parent bean factory, or {@code null} if there is none.
     * @return
     */
    @Nullable
    BeanFactory getParentBeanFactory();

    /**
     * return whether the local bean factory contains a bean of the given name
     * 返回本地bean工厂是否包含给定名称的bean
     * @param name
     * @return
     */
    boolean containsLocalBean(String name);
















}
