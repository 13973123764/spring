package org.springframework.context.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.io.Serializable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午4:42
 */
public class CommonAnnotationBeanPostProcessor extends InitDestroyAnnotationBeanPostProcessor
                implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable {
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }















}
