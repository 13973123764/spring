package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午8:54
 */
public class ContextClosedEvent extends ApplicationContextEvent{

    /**
     * Creates a new ContextClosedEvent.
     * @param source the {@code ApplicationContext} that has been closed
     * (must not be {@code null})
     */
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }

}
