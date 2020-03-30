package org.springframework.context;

/**
 * Interface that encapsulates event publication functionality
 */
@FunctionalInterface
public interface ApplicationEventPublisher {

    default void publishEvent(ApplicationEvent event){
        publishEvent((Object) event);
    }

    void publishEvent(Object event);

}
