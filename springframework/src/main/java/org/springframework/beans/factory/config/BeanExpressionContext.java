package org.springframework.beans.factory.config;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:34
 */
public class BeanExpressionContext {

    private final ConfigurableBeanFactory beanFactory;
    @Nullable
    private final Scope scope;

    public final ConfigurableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public BeanExpressionContext(ConfigurableBeanFactory beanFactory, @Nullable Scope scope) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
        this.scope = scope;
    }
}
