package org.springframework.core.type.filter;

import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:13
 */
public class AnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter{

    private final Class<? extends Annotation> annotationType;

    private final boolean considerMetaAnnotations;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this(annotationType, true, false);
    }

    public AnnotationTypeFilter(
            Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces) {

        super(annotationType.isAnnotationPresent(Inherited.class), considerInterfaces);
        this.annotationType = annotationType;
        this.considerMetaAnnotations = considerMetaAnnotations;
    }


    public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
        this(annotationType, considerMetaAnnotations, false);
    }


    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return false;
    }
}
