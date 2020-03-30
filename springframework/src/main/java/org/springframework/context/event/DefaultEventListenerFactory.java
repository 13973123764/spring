package org.springframework.context.event;

import org.springframework.core.Ordered;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午4:54
 */
public class DefaultEventListenerFactory implements EventListenerFactory, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }
}
