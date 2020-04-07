package org.springframework.context.support;

import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午4:51
 */
class ApplicationListenerDetector implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor {

    private final transient AbstractApplicationContext applicationContext;

    public ApplicationListenerDetector(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
