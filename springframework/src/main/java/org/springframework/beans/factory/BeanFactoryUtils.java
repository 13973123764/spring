package org.springframework.beans.factory;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午9:34
 */
public abstract class BeanFactoryUtils {

    /** bean名称转换缓存池 */
    private static final Map<String, String> transformedBeanNameCache = new ConcurrentHashMap<>();


    /**
     * bean名称转换
     * @param name  bean名称
     * @return
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "name must not be null");
        // 如果不是以 & 开始则直接返回bean名称
        if (!name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            return name;
        }
        // 如果bean名称缓存中不存在则判断 bean名称并转换
        return transformedBeanNameCache.computeIfAbsent(name, beanName -> {
            do {
                beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
            }
            while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
            return beanName;
        });
    }















}
