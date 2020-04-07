package org.springframework.util;

import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午4:04
 */
@FunctionalInterface
public interface StringValueResolver {

    @Nullable
    String resolveStringValue(String strVal);

}
