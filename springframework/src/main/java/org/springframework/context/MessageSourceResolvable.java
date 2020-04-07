package org.springframework.context;

import org.springframework.lang.Nullable;

/**
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:00
 */
@FunctionalInterface
public interface MessageSourceResolvable {

    @Nullable
    String[] getCodes();

    @Nullable
    default Object[] getArguments() {
        return null;
    }

    @Nullable
    default String getDefaultMessage() {
        return null;
    }
}
