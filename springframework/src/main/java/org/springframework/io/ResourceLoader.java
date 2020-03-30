package org.springframework.io;

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



}
