package org.springframework.beans.factory.support;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:07
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    /** 单例对象缓存, bean实例为bean的名称 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** 当前创建的 bean名称  */
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** 早期的单例对象缓存, bean实例为bean名称 */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /** 单例工厂缓存  */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    @Override
    @Nullable
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    @Nullable
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        // 从单例对象map 中取出
        Object singletonObject = this.singletonObjects.get(beanName);
        // 如果map中没有包含对象 并且 是处理当前正在创建
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            // 锁定单例对象map
            synchronized (this.singletonObjects) {
                // 从早期单例对象map中获取 bean
                singletonObject = this.earlySingletonObjects.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    // 从单例工厂中获得对象工厂
                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        singletonObject = singletonFactory.getObject();
                        // 存入早期单例对象map
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        // 从单例工程删除该 bean
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject;
    }

    /**
     * 返回当前是否正在创建指定的单例bean
     * @param beanName  bean的名称
     * @return
     */
    public boolean isSingletonCurrentlyInCreation(String beanName){
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }























}
