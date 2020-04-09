package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.TypeConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import java.security.AccessController;
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

        // 急切地检查单例缓存中手动注册的单例对象。
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }

    }


    /**
     * 获取给定bean实例的对象， 无论改bean实例化它自身 或者 它
     * @param sharedInstance
     * @param name
     * @param beanName
     * @param mbd
     * @return
     */
    protected abstract Object getObjectForBeanInstance(Object sharedInstance, String name, String beanName, @Nullable RootBeanDefinition mbd){

        BeanFactoryUtils.isFactoryDereference(name)


    }

    /**
     * bean名称状态
     * @param name  真实bean名称
     * @return
     */
    protected String transformedBeanName(String name) {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }
















}
