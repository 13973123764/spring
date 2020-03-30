package org.springframework.core;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:36
 */
public class OrderComparator implements Comparator<Object> {

    /** Shared default instance of {@code OrderComparator}. (默认的共享实例) */
    public static final OrderComparator INSTANCE = new OrderComparator();

    @Override
    public int compare(@Nullable Object o1,@Nullable Object o2) {
        return 0;
    }

    private int doCompare(@Nullable Object o1, @Nullable Object o2, @Nullable OrderSourceProvider sourceProvider){
        boolean p1 = (o1 instanceof PriorityOrdered);
        boolean p2 = (o2 instanceof PriorityOrdered);
        if (p1 && !p2) {
            return -1;
        }
        else if (p2 && !p1){
            return 1;
        }
        int i1 = getOrder(o1, sourceProvider);
        int i2 = getOrder(o2, sourceProvider);
        return Integer.compare(i1, i2);
    }

    private int getOrder(@Nullable Object obj, @Nullable OrderSourceProvider sourceProvider){
        Integer order = null;
        if (obj != null && sourceProvider != null) {
            Object orderSource = sourceProvider.getOrderSource(obj);
            if (orderSource != null) {
                if (orderSource.getClass().isArray()) {
                    Object[] sources = ObjectUtils.toObjectArray(orderSource);
                    for (Object source : sources) {
                        order = findOrder(source);
                        if (order != null) {
                            break;
                        }
                    }
                }
                else {
                    order = findOrder(orderSource);
                }
            }
        }
        return (order != null ? order : getOrder(obj));
    }

    protected int getOrder(@Nullable Object obj) {
        if (obj != null) {
            Integer order = findOrder(obj);
            if (order != null) {
                return order;
            }
        }
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Nullable
    protected Integer findOrder(Object obj) {
        return (obj instanceof Ordered ? ((Ordered) obj).getOrder() : null);
    }



    /**
     * Return an order source for the specified object, i.e. an object that
     * should be checked for an order value as a replacement to the given object.
     * <p>Can also be an array of order source objects.
     * <p>If the returned object does not indicate any order, the comparator
     * will fall back to checking the original object.
     * @param obj the object to find an order source for
     * @return the order source for that object, or {@code null} if none found
     */
    @FunctionalInterface
    public interface OrderSourceProvider {
        @Nullable
        Object getOrderSource(Object obj);
    }












}
