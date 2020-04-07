package org.springframework.core.io.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午10:04
 */
public class PathMatchingResourcePatternResolver implements ResourcePatternResolver {

    @Nullable
    private static Method equinoxResolveMethod;
    private final ResourceLoader resourceLoader;

    static {
        try {
            Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator",
                    PathMatchingResourcePatternResolver.class.getClassLoader());
        }
        catch (Throwable ex){
            equinoxResolveMethod = null;
        }
    }

    public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return new Resource[0];
    }

    @Override
    public Resource getResource(String location) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }
}
