package org.springframework.context.annotation;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * A component provider that provides candidate components from a base package.
 * Candidate components are identified by applying exclude and include filter.
 * 候选组件通过应用 exclude 和 include 过滤器来识别
 * @author ZhouFan
 * @Date 2020/03/28 下午6:34
 */
public class ClassPathScanningCandidateComponentProvider implements EnvironmentCapable, ResourceLoaderAware {

    /** 包含过滤 */
    private final List<TypeFilter> includeFilters = new LinkedList<>();
    /** 排除过滤 */
    private final List<TypeFilter> excludeFilters = new LinkedList<>();

    @Override
    public Environment getEnvironment() {
        return null;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    protected void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
        ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
        try {
            this.includeFilters.add(new AnnotationTypeFilter(
                    ((Class<? extends Annotation>) ClassUtils.forName("javax.annotation.ManagedBean", cl)), false));
        }
        catch (ClassNotFoundException ex) {
            // JSR-250 1.1 API (as included in Java EE 6) not available - simply skip.
        }
        try {
            this.includeFilters.add(new AnnotationTypeFilter(
                    ((Class<? extends Annotation>) ClassUtils.forName("javax.inject.Named", cl)), false));
        }
        catch (ClassNotFoundException ex) {
            // JSR-330 API not available - simply skip.
        }
    }
}
