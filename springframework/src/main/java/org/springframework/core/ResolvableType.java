package org.springframework.core;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Encapsulates a Java {@link java.lang.reflect.Type}, providing access to
 * {@link #getSuperType() supertypes}, {@link #getInterfaces() interfaces}, and
 * {@link #getGeneric(int...) generic parameters} along with the ability to ultimately
 * {@link #resolve() resolve} to a {@link java.lang.Class}.
 *
 */
public class ResolvableType implements Serializable {

    public static final ResolvableType NONE = new ResolvableType();


    /**
     * Private constructor used to create a new {@link ResolvableType} for cache key purposes,
     * with no upfront resolution.
     * 私有构造函数，用于创建新的可解析类型以用于缓存密钥，而无需预先解析
     */
    private ResolvableType(Type type, @Nullable TypePro){

    }


}
