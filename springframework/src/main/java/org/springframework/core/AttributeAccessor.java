package org.springframework.core;

import org.springframework.lang.Nullable;

public interface AttributeAccessor {

    String[] attributeNames();

    @Nullable
    Object getAttribute(String name);

    @Nullable
    Object removeAttribute(String name);

    void setAttribute(String name, @Nullable Object value);

}
