package org.springframework.beans.factory.support;

import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MethodOverrides {

    private final Set<MethodOverride> overrides = new CopyOnWriteArraySet<>();

    public boolean isEmpty() {
        return this.overrides.isEmpty();
    }

    MethodOverrides(){

    }


    public MethodOverrides(MethodOverrides other) {
        addOverrides(other);
    }


    public void addOverrides(@Nullable MethodOverrides other) {
        if (other != null) {
            this.overrides.addAll(other.overrides);
        }
    }


}
