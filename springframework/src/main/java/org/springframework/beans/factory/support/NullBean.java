package org.springframework.beans.factory.support;

import org.springframework.lang.Nullable;

final class NullBean {

    NullBean(){
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (this == obj || obj == null);
    }
}
