package org.springframework.context.expression;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:15
 */
public class BeanFactoryResolver implements BeanResolver {

    private final BeanFactory beanFactory;

    public BeanFactoryResolver(BeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

}
