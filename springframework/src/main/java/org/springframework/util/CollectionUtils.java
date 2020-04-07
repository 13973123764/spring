package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.Collection;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:24
 */
public abstract class CollectionUtils {


    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
