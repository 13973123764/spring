package org.springframework.beans.support;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ResourceLoader;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午2:59
 */
public class ResourceEditorRegistrar implements PropertyEditorRegistrar {

    private final PropertyResolver propertyResolver;

    private final ResourceLoader resourceLoader;

    public ResourceEditorRegistrar(ResourceLoader resourceLoader, PropertyResolver propertyResolver) {
        this.resourceLoader = resourceLoader;
        this.propertyResolver = propertyResolver;
    }
}
