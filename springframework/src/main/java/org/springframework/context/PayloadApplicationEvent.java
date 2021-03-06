package org.springframework.context;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午10:26
 */
public class PayloadApplicationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    private final T payload;

    /**
     * Create a new PayloadApplicationEvent.
     * @param source the object on which the event initially occurred (never {@code null})
     * @param payload the payload object (never {@code null})
     */
    public PayloadApplicationEvent(Object source, T payload) {
        super(source);
        Assert.notNull(payload, "Payload must not be null");
        this.payload = payload;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getPayload()));
    }

    /**
     * Return the payload of the event.
     */
    public T getPayload() {
        return this.payload;
    }

}
