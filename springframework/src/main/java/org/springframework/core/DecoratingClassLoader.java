package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午4:56
 */
public abstract class DecoratingClassLoader extends ClassLoader{

    public DecoratingClassLoader(@Nullable ClassLoader parent) {
        super(parent);
    }

}
