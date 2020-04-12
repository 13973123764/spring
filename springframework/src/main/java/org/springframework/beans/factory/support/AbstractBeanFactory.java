package org.springframework.beans.factory.support;

import com.sun.corba.se.impl.io.TypeMismatchException;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.*;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.TypeConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Policy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:02
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /** Parent bean factory, for bean inheritance support  bean工程父类， 为bean内部支持 */
    @Nullable
    private BeanFactory parentBeanFactory;

    /** ClassLoader to resolve bean class names with, if necessary 类加载器来解析bean的类名，如果需要*/
    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /** ClassLoader to temporarily resolve bean class names with, if necessary */
    @Nullable
    private ClassLoader tempClassLoader;

    /** Whether to cache bean metadata or rather reobtain it for every access */
    @Nullable
    private boolean cacheBeanMetadata = true;

    /** resolution strategy for expressions in bean definition values. */
    /** bean定义值中的表达解析策略. */
    private BeanExpressionResolver beanExpressionResolver;

    /** Spring ConversionService to use instead of PropertyEditors */
    /**  */
    @Nullable
    private ConversionService conversionService;

    /** Custom PropertyEditorRegistrars to apply to the beans of this factory. */
    /** 给当前工厂bean应用客户端属性设置登记器 */
    private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<>(4);

    /** Custom PropertyEditors to apply to the beans of this factory */
    /** 给当前工厂bean应用客户端属性编辑器 */
    private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>(4);

    /** A custom TypeConverter to use, overriding the default PropertyEditor mechanism */
    @Nullable
    private TypeConverter typeConverter;

    /** String resolvers to apply e.g. to annotation attribute values. */
    private final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList<>();

    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    private volatile boolean hashInstantiationAwareBeanPostProcessors;

    private final Map<String, Scope> scopes = new LinkedHashMap<>(8);

    /** Security context used when running with a SecurityManager. */
    @Nullable
    private SecurityContextProvider securityContextProvider;

    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);

    private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));

    private final ThreadLocal<Object> prototypesCurrentlyInCreation =
            new NamedThreadLocal<>("Prototype beans currently in creation");

    @Override
    public AccessControlContext getAccessControlContext() {
        return (this.securityContextProvider != null ?
                this.securityContextProvider.getAccessControlContext() :
                AccessController.getContext());
    }

    /**
     * 获取bean后置处理器总数
     * @return
     */
    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return doGetBean(name, requiredType, null, false);
    }

    /**
     * 获取bean
     * @param name              bean真实名称
     * @param requiredType      类型
     * @param args              参数
     * @param typeCheckOnly     是否检查
     * @param <T>               返回实例对象
     * @return
     */
    protected <T> T doGetBean(final String name,@Nullable final Class<T> requiredType,
                              @Nullable final Object[] args, boolean typeCheckOnly) {
        // 转换bean名称
        final String beanName = transformedBeanName(name);
        // 定义bean对象
        Object bean;

        // 检查单例缓存中手动注册的单例对象。
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }
        else {
            if (isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }
            // 检查当前是否已经存在该bean定义
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
                String nameToLookup = originalBeanName(name);
                if (parentBeanFactory instanceof AbstractBeanFactory) {
                    return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
                            nameToLookup, requiredType, args, typeCheckOnly);
                }
                else if (args != null) {
                    // Delegation to parent with explicit args.
                    return (T) parentBeanFactory.getBean(nameToLookup, args);
                }
                else {
                    // No args -> delegate to standard getBean method.
                    return parentBeanFactory.getBean(nameToLookup, requiredType);
                }
            }
            if (!typeCheckOnly) {
                markBeanAsCreated(beanName);
            }
            try {
                final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                checkMergedBeanDefinition(mbd, beanName, args);
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        registerDependentBean(dep, beanName);
                        try {
                            getBean(dep);
                        }
                        catch (NoSuchBeanDefinitionException ex) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                        }
                    }
                }
                if (mbd.isSingleton()) {
                    sharedInstance = getSingleton(beanName, () -> {
                        try {
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    });
                    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                }
                else if (mbd.isPrototype()) {
                    // It's a prototype -> create a new instance.
                    Object prototypeInstance = null;
                    try {
                        beforePrototypeCreation(beanName);
                        prototypeInstance = createBean(beanName, mbd, args);
                    }
                    finally {
                        afterPrototypeCreation(beanName);
                    }
                    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                }
                else {
                    String scopeName = mbd.getScope();
                    final Scope scope = this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }
                    try {
                        Object scopedInstance = scope.get(beanName, () -> {
                            beforePrototypeCreation(beanName);
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            finally {
                                afterPrototypeCreation(beanName);
                            }
                        });
                        bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    }
                    catch (IllegalStateException ex) {
                        throw new BeanCreationException(beanName,
                                "Scope '" + scopeName + "' is not active for the current thread; consider " +
                                        "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                                ex);
                    }
                }
            }
            catch (BeansException ex) {
                cleanupAfterBeanCreationFailure(beanName);
                throw ex;
            }
        }

        // Check if required type matches the type of the actual bean instance.
        if (requiredType != null && !requiredType.isInstance(bean)) {
            try {
                T convertedBean = getTypeConverter().convertIfNecessary(bean, requiredType);
                if (convertedBean == null) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                }
                return convertedBean;
            }
            catch (TypeMismatchException ex) {

            }
        }


    }


    @SuppressWarnings("unchecked")
    protected void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
        }
        else if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }

    protected void markBeanAsCreated(String beanName) {
        if (!this.alreadyCreated.contains(beanName)) {
            synchronized (this.mergedBeanDefinitions) {
                if (!this.alreadyCreated.contains(beanName)) {
                    // Let the bean definition get re-merged now that we're actually creating
                    // the bean... just in case some of its metadata changed in the meantime.
                    clearMergedBeanDefinition(beanName);
                    this.alreadyCreated.add(beanName);
                }
            }
        }
    }

    protected void clearMergedBeanDefinition(String beanName) {
        this.mergedBeanDefinitions.remove(beanName);
    }


    protected boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return (curVal != null &&
                (curVal.equals(beanName) || (curVal instanceof Set && ((Set<?>) curVal).contains(beanName))));
    }


    protected void cleanupAfterBeanCreationFailure(String beanName) {
        synchronized (this.mergedBeanDefinitions) {
            this.alreadyCreated.remove(beanName);
        }
    }

    protected void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
        }
        else if (curVal instanceof String) {
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
        }
        else {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.add(beanName);
        }
    }


    /**
     * 获取给定bean实例的对象， 无论改bean实例化它自身 或者 它
     * @param beanInstance
     * @param name
     * @param beanName
     * @param mbd
     * @return
     */
    protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd){
        if (BeanFactoryUtils.isFactoryDereference(name)) {
            if (beanInstance instanceof NullBean) {
                return beanInstance;
            }
            if (!(beanInstance instanceof FactoryBean)) {
                throw new BeanIsNotAFactoryException(beanName, beanInstance.getClass());
            }
        }
        if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
            return beanInstance;
        }
        Object object = null;
        if (mbd == null) {
            object = getCachedObjectForFactoryBean(beanName);
        }
        if (object == null) {
            FactoryBean<?> factory =(FactoryBean<?>) beanInstance;
            if (mbd == null && containsBeanDefinition(beanName)) {
                mbd = getMergedLocalBeanDefinition(beanName);
            }
            boolean synthetic = (mbd != null && mbd.isSynthetic());
            object = getObjectFromFactoryBean(factory, beanName, !synthetic);
        }
        return object;
    }

    protected String originalBeanName(String name) {
        String beanName = transformedBeanName(name);
        if (name.startsWith(FACTORY_BEAN_PREFIX)) {
            beanName = FACTORY_BEAN_PREFIX + beanName;
        }
        return beanName;
    }

    protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, @Nullable Object[] args)
            throws BeanDefinitionStoreException {

        if (mbd.isAbstract()) {
            throw new BeanIsAbstractException(beanName);
        }
    }

    /**
     * bean名称状态
     * @param name  真实bean名称
     * @return
     */
    protected String transformedBeanName(String name) {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }

    protected abstract boolean containsBeanDefinition(String beanName);


    protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException{
        RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
        if (mbd != null) {
            return mbd;
        }
        getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
    }


    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;


    protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd) throws BeanDefinitionStoreException {
        return getMergedBeanDefinition(beanName, bd, null);
    }


    /**
     * 如果给定的bean定义对象是一个bean definition子类,则通过与父类合并
     * 则从给定的bean定义返回一个 rootBeanDefinition
     * @param beanName
     * @param bd
     * @param containingBd
     * @return
     * @throws BeanDefinitionStoreException
     */
    protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, @Nullable BeanDefinition containingBd) throws BeanDefinitionStoreException{
        synchronized (this.mergedBeanDefinitions) {
            // bean定义根节点
            RootBeanDefinition mbd = null;
            if (containingBd == null) {
                mbd = this.mergedBeanDefinitions.get(beanName);
            }
            if (mbd == null) {
                String parentName = bd.getParentName();
            }
            if (bd.getParentName() == null) {
                if (bd instanceof RootBeanDefinition) {
                    mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
                }
                else {
                    mbd = new RootBeanDefinition(bd);
                }
            }
        }
    }


    protected abstract Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
            throws BeanCreationException;



}
