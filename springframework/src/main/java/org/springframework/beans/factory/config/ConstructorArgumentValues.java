package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.Mergeable;
import org.springframework.lang.Nullable;

import java.util.*;

public class ConstructorArgumentValues {

    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();

    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();



    public boolean isEmpty() {
        return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
    }

    public ConstructorArgumentValues(ConstructorArgumentValues original) {
        addArgumentValues(original);
    }

    public void addArgumentValues(@Nullable ConstructorArgumentValues other) {
        if (other != null) {
            other.indexedArgumentValues.forEach(
                    (index, argValue) -> addOrMergeIndexedArgumentValue(index, argValue.copy())
            );
            other.genericArgumentValues.stream()
                    .filter(valueHolder -> !this.genericArgumentValues.contains(valueHolder))
                    .forEach(valueHolder -> addOrMergeGenericArgumentValue(valueHolder.copy()));
        }
    }


    private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
        if (newValue.getName() != null) {
            for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();) {
                ValueHolder currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    if (newValue.getValue() instanceof Mergeable) {
                        Mergeable mergeable = (Mergeable) newValue.getValue();
                        if (mergeable.isMergeEnabled()) {
                            newValue.setValue(mergeable.merge(currentValue.getValue()));
                        }
                    }
                }
            }
        }
    }

    private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue){
        ValueHolder currentValue = this.indexedArgumentValues.get(key);
        if (currentValue != null && newValue.getValue() instanceof Mergeable) {
            Mergeable mergeable = (Mergeable) newValue.getValue();
            if (mergeable.isMergeEnabled()) {
                newValue.setValue(mergeable.merge(currentValue.getValue()));
            }
        }
        this.indexedArgumentValues.put(key, newValue);
    }


    public static class ValueHolder implements BeanMetadataElement{
        @Nullable
        private Object value;

        @Nullable
        private String type;

        @Nullable
        private String name;

        @Nullable
        private Object source;

        private boolean converted = false;

        public void setName(@Nullable String name) {
            this.name = name;
        }

        @Nullable
        public String getName() {
            return this.name;
        }

        public void setValue(@Nullable Object value) {
            this.value = value;
        }

        @Nullable
        public Object getValue() {
            return this.value;
        }

        @Nullable
        @Override
        public Object getSource() {
            return this.source;
        }

        public void setSource(@Nullable Object source) {
            this.source = source;
        }


        public ValueHolder(@Nullable Object value, @Nullable String type, @Nullable String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public ValueHolder copy() {
            ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
            copy.setSource(this.source);
            return copy;
        }
    }
}
