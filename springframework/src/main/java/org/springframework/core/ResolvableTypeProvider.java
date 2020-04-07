package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 上午11:08
 */
public interface ResolvableTypeProvider {

    @Nullable
    ResolvableType getResolvableType();
}
