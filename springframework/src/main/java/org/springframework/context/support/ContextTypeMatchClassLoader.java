package org.springframework.context.support;

import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.SmartClassLoader;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午4:56
 */
class ContextTypeMatchClassLoader extends DecoratingClassLoader implements SmartClassLoader {

    public ContextTypeMatchClassLoader(@Nullable ClassLoader parent) {
        super(parent);
    }
}
