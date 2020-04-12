package org.springframework.beans;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MutablePropertyValues implements PropertyValues, Serializable {

    private final List<PropertyValue> propertyValueList;

    @Override
    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }

    public MutablePropertyValues(@Nullable PropertyValues original) {
        // We can optimize this because it's all new:
        // There is no replacement of existing property values.
        if (original != null) {
            PropertyValue[] pvs = original.getPropertyValues();
            this.propertyValueList = new ArrayList<>(pvs.length);
            for (PropertyValue pv : pvs) {
                this.propertyValueList.add(new PropertyValue(pv));
            }
        }
        else {
            this.propertyValueList = new ArrayList<>(0);
        }
    }

}
