package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:03
 */
public class TypedValue {

    @Nullable
    private final Object value;

    @Nullable
    private TypeDescriptor typeDescriptor;

    public TypedValue(@Nullable Object value) {
        this.value = value;
        this.typeDescriptor = null;  // initialized when/if requested
    }
}

