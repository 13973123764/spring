package org.springframework.context;

import java.util.EventListener;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:06
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);

}
