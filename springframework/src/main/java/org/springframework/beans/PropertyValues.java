package org.springframework.beans;

import java.io.Serializable;

public interface PropertyValues extends BeanMetadataAttributeAccessor implements Serializable {


    PropertyValue[] getPropertyValues();

    boolean isEmpty();

}
