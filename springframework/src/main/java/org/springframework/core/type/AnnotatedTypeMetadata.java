package org.springframework.core.type;

import org.springframework.lang.Nullable;

import java.util.Map;

public interface AnnotatedTypeMetadata {

    @Nullable
    Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString);
}
