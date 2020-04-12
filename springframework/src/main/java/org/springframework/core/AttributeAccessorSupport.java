package org.springframework.core;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Support class for {@link AttributeAccessor AttributeAccessors}
 * providing a base implementation of all methods. To be extended by subclass
 * 提供所有方法的基本实现，由子类扩展
 */
public abstract class AttributeAccessorSupport implements AttributeAccessor, Serializable {


    private final Map<String, Object> attributes = new LinkedHashMap<>();


    protected void copyAttributesFrom(AttributeAccessor source) {
        Assert.notNull(source, "Source must not be null");
        String[] attributeNames = source.attributeNames();
        for (String attributeName : attributeNames) {
            setAttribute(attributeName, source.getAttribute(attributeName));
        }
    }

    @Override
    public void setAttribute(String name, @Nullable Object value) {
        Assert.notNull(name, "Name must not be null");
        if (value != null) {
            this.attributes.put(name, value);
        }
        else {
            removeAttribute(name);
        }
    }

    @Override
    @Nullable
    public Object removeAttribute(String name) {
        Assert.notNull(name, "Name must not be null");
        return this.attributes.remove(name);
    }

}
