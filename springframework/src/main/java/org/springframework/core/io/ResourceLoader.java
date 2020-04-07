package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

/**
 * Strategy interface for loading resources (e.. class path or file system
 * 用于加载资源（类路径下或者系统资源路径）
 * {@link org.springframework.core.io.support.ResourcePatternResolver} 拓展类.
 */
public interface ResourceLoader {

    /** 类路径前缀 */
    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    Resource getResource(String location);

    /**
     * Expose the ClassLoader used by this ResourceLoader.
     * <p>Clients which need to access the ClassLoader directly can do so
     * in a uniform manner with the ResourceLoader, rather than relying
     * on the thread context ClassLoader.
     * @return the ClassLoader
     * (only {@code null} if even the system ClassLoader isn't accessible)
     * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
     * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
     */
    @Nullable
    ClassLoader getClassLoader();

}
