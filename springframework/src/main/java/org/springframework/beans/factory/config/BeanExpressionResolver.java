package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:33
 */
public interface BeanExpressionResolver {

    @Nullable
    Object evaluate(@Nullable String value, BeanExpressionContext evalContext) throws BeansException;

}
