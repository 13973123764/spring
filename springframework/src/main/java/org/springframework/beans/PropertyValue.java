package org.springframework.beans;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;

public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

    private final String name;

    @Nullable
    private final Object value;

    private boolean optional = false;

    private boolean converted = false;

    @Nullable
    private Object convertedValue;

    /** Package-visible field that indicates whether conversion is necessary */
    @Nullable
    volatile Boolean conversionNecessary;

    /** Package-visible field for caching the resolved property path tokens */
    @Nullable
    transient volatile Object resolvedTokens;


    public PropertyValue(PropertyValue original) {
        Assert.notNull(original, "Original must not be null");
        this.name = original.getName();
        this.value = original.getValue();
        this.optional = original.isOptional();
        this.converted = original.converted;
        this.convertedValue = original.convertedValue;
        this.conversionNecessary = original.conversionNecessary;
        this.resolvedTokens = original.resolvedTokens;
        setSource(original.getSource());
        copyAttributesFrom(original);
    }


    public String getName() {
        return this.name;
    }

    @Nullable
    public Object getValue() {
        return this.value;
    }

    public boolean isOptional() {
        return this.optional;
    }


}
