package org.springframework.core;

import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午9:16
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {

    private final String name;

    public NamedThreadLocal(String name) {
        Assert.hasText(name, "Name must not be empty");
        this.name = name;
    }

}
