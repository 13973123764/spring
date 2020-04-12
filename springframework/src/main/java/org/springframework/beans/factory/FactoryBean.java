package org.springframework.beans.factory;

import org.springframework.lang.Nullable;

public interface FactoryBean<T> {

    default boolean isSingleton() {
        return true;
    }


    @Nullable
    T getObject() throws Exception;

}
