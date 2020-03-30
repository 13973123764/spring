package org.springframework.beans;

import org.springframework.core.AttributeAccessorSupport;
import org.springframework.lang.Nullable;

public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {

    @Nullable
    private Object source;

    @Override
    public Object getSource() {
        return this.source;
    }

    public void setSource(@Nullable Object source) {
        this.source = source;
    }
}
