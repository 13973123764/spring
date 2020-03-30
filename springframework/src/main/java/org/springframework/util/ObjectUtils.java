package org.springframework.util;

import org.springframework.lang.Nullable;

import java.lang.reflect.Array;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午10:42
 */
public class ObjectUtils {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static Object[] toObjectArray(@Nullable Object source){
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        // 为空返回空数组
        if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        }
        // 不是数组抛出异常
        if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("source is not an array: " + source);
        }
        // 判断数组长度
        int length = Array.getLength(source);
        if (length == 0) {
            return EMPTY_OBJECT_ARRAY;
        }
        Class<?> wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        return newArray;
    }


















}
