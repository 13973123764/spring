package org.springframework.context;

import org.springframework.beans.factory.Aware;

/**
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午4:41
 */
public interface ApplicationEventPublisherAware extends Aware {

    void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher);

}
