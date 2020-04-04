package org.springframework.util;

import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/27 下午10:56
 */
public abstract class Assert {

    /**
     * Assert that an object is not null
     * 断言该对象不为空
     * @param object
     * @param message
     */
    public static void notNull(@Nullable Object object, String message){
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void isTrue(boolean b, String s) {

    }
}
