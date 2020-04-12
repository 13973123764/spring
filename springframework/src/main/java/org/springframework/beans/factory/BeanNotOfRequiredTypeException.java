package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.util.ClassUtils;

public class BeanNotOfRequiredTypeException extends BeansException {

    private String beanName;
    private Class<?> requiredType;
    private Class<?> actualType;

    public BeanNotOfRequiredTypeException(String beanName, Class<?> requiredType, Class<?> actualType) {
        super("Bean named '" + beanName + "' is expected to be of type '" + ClassUtils.getQualifiedName(requiredType) +
                "' but was actually of type '" + ClassUtils.getQualifiedName(actualType) + "'");
        this.beanName = beanName;
        this.requiredType = requiredType;
        this.actualType = actualType;
    }


    public String getBeanName() {
        return beanName;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
