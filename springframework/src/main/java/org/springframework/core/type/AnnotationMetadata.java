package org.springframework.core.type;

import java.util.Set;

public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {

    /**
     * get the fully qualified class names of all annotation types that
     * 获取所有注解类型的完整类名
     * @return
     */
    Set<String> getAnnotationTypes();

    /**
     * Get the fully qualified class names of all meta-annotation types that
     * 获取所有元注解类型的完整类名
     * @param annotationName
     * @return
     */
    Set<String> getMetaAnnotationTypes(String annotationName);

}
