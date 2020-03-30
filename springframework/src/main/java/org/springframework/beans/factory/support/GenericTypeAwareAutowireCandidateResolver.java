package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午11:54
 */
public class GenericTypeAwareAutowireCandidateResolver extends SimpleAutowireCandidateResolver
                                        implements BeanFactoryAware {
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
