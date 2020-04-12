package org.springframework.beans.factory.support;

import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;


public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor implements BeanDefinition, Cloneable {

    public static final String SCOPE_DEFAULT = "";

    public static final int DEPENDENCY_CHECK_NONE = 0;

    private boolean autowireCandidate = true;

    private boolean primary = false;

    @Nullable
    private String[] dependsOn;

    private volatile Object beanClass;

    @Nullable
    private String factoryBeanName;

    @Nullable
    private String factoryMethodName;

    @Nullable
    private MutablePropertyValues propertyValues;

    private MethodOverrides methodOverrides = new MethodOverrides();

    public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;

    private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<>();

    private boolean abstractFlag = false;

    private boolean lazyInit = false;

    private int role = BeanDefinition.ROLE_APPLICATION;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    private int dependencyCheck = DEPENDENCY_CHECK_NONE;

    private boolean synthetic = false;

    @Nullable
    private String scope = SCOPE_DEFAULT;

    private int autowireMode = AUTOWIRE_NO;

    @Nullable
    private Resource resource;

    @Nullable
    private ConstructorArgumentValues constructorArgumentValues;

    protected AbstractBeanDefinition() {
        this(null, null);
    }

    protected AbstractBeanDefinition(@Nullable ConstructorArgumentValues cargs, @Nullable MutablePropertyValues pvs) {
        this.constructorArgumentValues = cargs;
        this.propertyValues = pvs;
    }

    protected AbstractBeanDefinition(BeanDefinition original) {
        // 父类名称
        setParentName(original.getParentName());
        // 类名
        setBeanClassName(original.getBeanClassName());
        // 范围
        setScope(original.getScope());
        // 是否是抽象的
        setAbstract(original.isAbstract());
        // 是否是懒加载
        setLazyInit(original.isLazyInit());
        // 工厂bean名称
        setFactoryBeanName(original.getFactoryBeanName());
        // 工厂方法名
        setFactoryMethodName(original.getFactoryMethodName());
        // 角色名
        setRole(original.getRole());
        // 资源名
        setSource(original.getSource());
        // 复制属性
        copyAttributesFrom(original);

        // 如果是抽象bean对象的实例
        if (original instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
            if (originalAbd.hashBeanClass()) {
                setBeanClass(originalAbd.getBeanClass());
            }
            // 构造参数值
            if (originalAbd.hasConstructorArgumentValues()) {
                setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
            }
            // 属性值
            if (originalAbd.hasPropertyValues()) {
                setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
            }
            // 是否有方法重写
            if (originalAbd.hasMethodOverrides()) {
                setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
            }
            // 自动注入模式
            setAutowireMode(originalAbd.getAutowireMode());
            // 依赖检查
            setDependencyCheck(originalAbd.getDependencyCheck());
            setDependsOn(originalAbd.getDependsOn());
            setAutowireCandidate(originalAbd.isAutowireCandidate());
            setPrimary(originalAbd.isPrimary());
            copyQualifiersFrom(originalAbd);
        }

    }


    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setMethodOverrides(MethodOverrides methodOverrides) {
        this.methodOverrides = methodOverrides;
    }

    @Override
    public void setDependsOn(@Nullable String... dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    @Override
    public boolean isPrimary() {
        return this.primary;
    }


    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

    /**
     * return the current bean class name of this bean definition
     * 从这个bean定义中返回当前bean的类名
     * @return
     */
    @Override
    @Nullable
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        }
        else {
            return (String) beanClassObject;
        }
    }

    public int getDependencyCheck() {
        return this.dependencyCheck;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    @Override
    public boolean hasPropertyValues() {
        return (this.propertyValues != null && !this.propertyValues.isEmpty());
    }

    public boolean hasMethodOverrides() {
        return !this.methodOverrides.isEmpty();
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return (this.constructorArgumentValues != null && !this.constructorArgumentValues.isEmpty());
    }

    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    public int getAutowireMode() {
        return this.autowireMode;
    }

    public MethodOverrides getMethodOverrides() {
        return this.methodOverrides;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public void setBeanClass(@Nullable Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void copyQualifiersFrom(AbstractBeanDefinition source) {
        Assert.notNull(source, "Source must not be null");
        this.qualifiers.putAll(source.qualifiers);
    }

    @Override
    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public int getRole() {
        return this.role;
    }

    @Override
    @Nullable
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    @Override
    public boolean isAutowireCandidate() {
        return this.autowireCandidate;
    }

    public boolean isSynthetic(){
        return this.synthetic;
    }

    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public void setBeanClassName(@Nullable String beanClassName) {
        this.beanClass = beanClassName;
    }

    @Override
    public void setScope(@Nullable String scope) {
        this.scope = scope;
    }

    @Override
    public void setFactoryBeanName(@Nullable String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public void setFactoryMethodName(@Nullable String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }


    public boolean hashBeanClass(){
        return (this.beanClass instanceof Class);
    }


    @Override
    @Nullable
    public String getResourceDescription() {
        return (this.resource != null ? this.resource.getDescription() : null);
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope) || SCOPE_DEFAULT.equals(this.scope);
    }


























}
