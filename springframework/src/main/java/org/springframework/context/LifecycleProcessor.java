package org.springframework.context;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午9:55
 */
public interface LifecycleProcessor extends Lifecycle{

    void onRefresh();

    void onClose();

}
