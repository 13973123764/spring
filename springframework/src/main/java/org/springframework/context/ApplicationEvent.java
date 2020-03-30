package org.springframework.context;

import java.util.EventObject;

/**
 * class to be extended by all application events.
 * Abstract as it doesn't make sense for generic events
 * to be published directly.
 *
 * 此类被所有应用事件扩展
 */
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 7099057708183571937L;

    /** system time when the event happened  记录事件发生的系统事件 */
    private final long timestamp;

    /**
     * 创建一个新的应用事件
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
        timestamp = System.currentTimeMillis();
    }

    public final long getTimestamp(){
        return this.timestamp;
    }

}
