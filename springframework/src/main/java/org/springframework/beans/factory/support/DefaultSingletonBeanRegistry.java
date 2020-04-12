package org.springframework.beans.factory.support;

import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:07
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    /** 单例对象缓存, bean实例为bean的名称 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** 当前创建的 bean名称  */
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** 早期的单例对象缓存, bean实例为bean名称 */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    private final Map<String, Object> disposableBeans = new LinkedHashMap<>();

    private boolean singletonsCurrentlyInDestruction = false;

    @Nullable
    private Set<Exception> suppressedExceptions;

    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    /** 单例工厂缓存  */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /** Map between dependent bean names: bean name --> Set of dependent bean names */
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

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


    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(beanName, "Bean name must not be null");
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                if (this.singletonsCurrentlyInDestruction) {
                    throw new BeanCreationNotAllowedException(beanName,
                            "Singleton bean creation not allowed while singletons of this factory are in destruction " +
                                    "(Do not request a bean from a BeanFactory in a destroy method implementation!)");
                }
                beforeSingletonCreation(beanName);
                boolean newSingleton = false;
                boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
                if (recordSuppressedExceptions) {
                    this.suppressedExceptions = new LinkedHashSet<>();
                }
                try {
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }
                catch (IllegalStateException ex) {
                    // Has the singleton object implicitly appeared in the meantime ->
                    // if yes, proceed with it since the exception indicates that state.
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        throw ex;
                    }
                }
                catch (BeanCreationException ex) {
                    if (recordSuppressedExceptions) {
                        for (Exception suppressedException : this.suppressedExceptions) {
                            ex.addRelatedCause(suppressedException);
                        }
                    }
                    throw ex;
                }
                finally {
                    if (recordSuppressedExceptions) {
                        this.suppressedExceptions = null;
                    }
                    afterSingletonCreation(beanName);
                }
                if (newSingleton) {
                    addSingleton(beanName, singletonObject);
                }
            }
            return singletonObject;
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    /**
     * 返回当前是否正在创建指定的单例bean
     * @param beanName  bean的名称
     * @return
     */
    public boolean isSingletonCurrentlyInCreation(String beanName){
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }


    @Override
    public final Object getSingletonMutex() {
        return this.singletonObjects;
    }


    protected void beforeSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }
    }

    protected void afterSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }

    protected boolean isDependent(String beanName, String dependentBeanName) {
        synchronized (this.dependentBeanMap) {
            return isDependent(beanName, dependentBeanName, null);
        }
    }

    private boolean isDependent(String beanName, String dependentBeanName, @Nullable Set<String> alreadySeen) {
        if (alreadySeen != null && alreadySeen.contains(beanName)) {
            return false;
        }
        String canonicalName = canonicalName(beanName);
        Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
        if (dependentBeans == null) {
            return false;
        }
        if (dependentBeans.contains(dependentBeanName)) {
            return true;
        }
        for (String transitiveDependency : dependentBeans) {
            if (alreadySeen == null) {
                alreadySeen = new HashSet<>();
            }
            alreadySeen.add(beanName);
            if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
                return true;
            }
        }
        return false;
    }


    public void registerDependentBean(String beanName, String dependentBeanName) {
        String canonicalName = canonicalName(beanName);

        synchronized (this.dependentBeanMap) {
            Set<String> dependentBeans =
                    this.dependentBeanMap.computeIfAbsent(canonicalName, k -> new LinkedHashSet<>(8));
            if (!dependentBeans.add(dependentBeanName)) {
                return;
            }
        }

        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean =
                    this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>(8));
            dependenciesForBean.add(canonicalName);
        }
    }


    public void destroySingleton(String beanName) {
        // Remove a registered singleton of the given name, if any.
        removeSingleton(beanName);

        // Destroy the corresponding DisposableBean instance.
        DisposableBean disposableBean;
        synchronized (this.disposableBeans) {
            disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
        }
        destroyBean(beanName, disposableBean);
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }


    protected void destroyBean(String beanName, @Nullable DisposableBean bean) {
        Set<String> dependencies;
        synchronized (this.dependentBeanMap) {
            // Within full synchronization in order to guarantee a disconnected Set
            dependencies = this.dependentBeanMap.remove(beanName);
        }
        if (dependencies != null) {
            for (String dependentBeanName : dependencies) {
                destroySingleton(dependentBeanName);
            }
        }
        // Actually destroy the bean now...
        if (bean != null) {
            try {
                bean.destroy();
            }
            catch (Throwable ex) {
                System.out.println("Destroy method on bean with name '" + beanName + "' threw an exception"+ ex);
            }
        }
        // Trigger destruction of contained beans...
        Set<String> containedBeans;
        synchronized (this.containedBeanMap) {
            // Within full synchronization in order to guarantee a disconnected Set
            containedBeans = this.containedBeanMap.remove(beanName);
        }

        if (containedBeans != null) {
            for (String containedBeanName : containedBeans) {
                destroySingleton(containedBeanName);
            }
        }

        // Remove destroyed bean from other beans' dependencies.
        synchronized (this.dependentBeanMap) {
            for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Set<String>> entry = it.next();
                Set<String> dependenciesToClean = entry.getValue();
                dependenciesToClean.remove(beanName);
                if (dependenciesToClean.isEmpty()) {
                    it.remove();
                }
            }
        }
        // Remove destroyed bean's prepared dependency information.
        this.dependenciesForBeanMap.remove(beanName);
    }


}
