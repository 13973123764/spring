package org.springframework.beans.factory.annotation;

import org.springframework.core.OrderComparator;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:32
 */
public class AnnotationAwareOrderComparator extends OrderComparator {

    /** Shared default instance of  默认的共享实例 */
    public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();

}
