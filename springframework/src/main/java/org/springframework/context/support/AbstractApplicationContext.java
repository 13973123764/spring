package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.beans.support.ApplicationContextAwareProcessor;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.*;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.expression.StandardBeanExpressionResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.io.DefaultResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
                                                        implements ConfigurableApplicationContext {

    /**
     * Name of the MessageSource bean in the factory.
     * If none is supplied, message resolution is delegated to the parent.
     * @see MessageSource
     */
    public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";

    /**
     * Name of the LifecycleProcessor bean in the factory.
     * If none is supplied, a DefaultLifecycleProcessor is used.
     */
    public static final String LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor";

    /**
     * Name of the ApplicationEventMulticaster bean in the factory.
     * If none is supplied, a default SimpleApplicationEventMulticaster is used.
     */
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    static {
        // Eagerly load the ContextClosedEvent class to avoid weird classloader issues
        // on application shutdown in WebLogic 8.1. (Reported by Dustin Woods.)
        ContextClosedEvent.class.getName();
    }

    /** Unique id for this context, if any. */
    private String id = ObjectUtils.identityToString(this);

    /** Display name. */
    private String displayName = ObjectUtils.identityToString(this);

    /** Parent context. 父级上下文 */
    @Nullable
    private ApplicationContext parent;

    /** Environment used by this context.  */
    @Nullable
    private ConfigurableEnvironment environment;

    /** BeanFactoryPostProcessors to apply on refresh. */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    /** System time in milliseconds when this context started. */
    private long startupDate;

    /** Flag that indicates whether this context is currently active. */
    private final AtomicBoolean active = new AtomicBoolean();

    /** Flag that indicates whether this context has been closed already. */
    private final AtomicBoolean closed = new AtomicBoolean();

    /** Synchronization monitor for the "refresh" and "destroy". */
    private final Object startupShutdownMonitor = new Object();

    /** Reference to the JVM shutdown hook, if registered. */
    @Nullable
    private Thread shutdownHook;

    /** ResourcePatternResolver used by this context. */
    private ResourcePatternResolver resourcePatternResolver;

    /** LifecycleProcessor for managing the lifecycle of beans within this context. */
    @Nullable
    private LifecycleProcessor lifecycleProcessor;

    /** MessageSource we delegate our implementation of this interface to. */
    @Nullable
    private MessageSource messageSource;

    /** Helper class used in event publishing. */
    @Nullable
    private ApplicationEventMulticaster applicationEventMulticaster;

    /** Statically specified listeners. */
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    /** Local listeners registered before refresh. */
    @Nullable
    private Set<ApplicationListener<?>> earlyApplicationListeners;

    /** ApplicationEvents published before the multicaster setup. */
    @Nullable
    private Set<ApplicationEvent> earlyApplicationEvents;

    /**
     * Create a new AbstractApplicationContext with no parent.
     */
    public AbstractApplicationContext() {
        this.resourcePatternResolver = getResourcePatternResolver();
    }

    /**
     * Create a new AbstractApplicationContext with the given parent context.
     * @param parent the parent context
     */
    public AbstractApplicationContext(@Nullable ApplicationContext parent) {
        this();
        setParent(parent);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getApplicationName() {
        return "";
    }

    /**
     * Set a friendly name for this context.
     * Typically done during initialization of concrete context implementations.
     * <p>Default is the object id of the context instance.
     */
    public void setDisplayName(String displayName) {
        Assert.hasLength(displayName, "Display name must not be empty");
        this.displayName = displayName;
    }

    /**
     * Return a friendly name for this context.
     * @return a display name for this context (never {@code null})
     */
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Return the parent context, or {@code null} if there is no parent
     * (that is, this context is the root of the context hierarchy).
     */
    @Override
    @Nullable
    public ApplicationContext getParent() {
        return this.parent;
    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = createEnvironment();
        }
        return this.environment;
    }

    /**
     * Create and return a new {@link StandardEnvironment}.
     * <p>Subclasses may override this method in order to supply
     * a custom {@link ConfigurableEnvironment} implementation.
     */
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }

    /**
     * Return this context's internal bean factory as AutowireCapableBeanFactory,
     * if already available.
     * @see #getBeanFactory()
     */
    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return getBeanFactory();
    }

    /**
     * Return the timestamp (ms) when this context was first loaded.
     */
    @Override
    public long getStartupDate() {
        return this.startupDate;
    }

    /**
     * Publish the given event to all listeners.
     * <p>Note: Listeners get initialized after the MessageSource, to be able
     * to access it within listener implementations. Thus, MessageSource
     * implementations cannot publish events.
     * @param event the event to publish (may be application-specific or a
     * standard framework event)
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        publishEvent(event, null);
    }

    /**
     * Publish the given event to all listeners.
     * <p>Note: Listeners get initialized after the MessageSource, to be able
     * to access it within listener implementations. Thus, MessageSource
     * implementations cannot publish events.
     * @param event the event to publish (may be an {@link ApplicationEvent}
     * or a payload object to be turned into a {@link PayloadApplicationEvent})
     */
    @Override
    public void publishEvent(Object event) {
        publishEvent(event, null);
    }

    /**
     * Publish the given event to all listeners.
     * @param event the event to publish (may be an {@link ApplicationEvent}
     * or a payload object to be turned into a {@link PayloadApplicationEvent})
     * @param eventType the resolved event type, if known
     * @since 4.2
     */
    protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
        Assert.notNull(event, "Event must not be null");

        // Decorate event as an ApplicationEvent if necessary
        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent) event;
        }
        else {
            applicationEvent = new PayloadApplicationEvent<>(this, event);
            if (eventType == null) {
                eventType = ((PayloadApplicationEvent<?>) applicationEvent).getResolvableType();
            }
        }

        // Multicast right now if possible - or lazily once the multicaster is initialized
        if (this.earlyApplicationEvents != null) {
            this.earlyApplicationEvents.add(applicationEvent);
        }
        else {
            getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
        }

        // Publish event via parent context as well...
        if (this.parent != null) {
            if (this.parent instanceof AbstractApplicationContext) {
                ((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
            }
            else {
                this.parent.publishEvent(event);
            }
        }
    }

    /**
     * Return the internal ApplicationEventMulticaster used by the context.
     * @return the internal ApplicationEventMulticaster (never {@code null})
     * @throws IllegalStateException if the context has not been initialized yet
     */
    ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
        if (this.applicationEventMulticaster == null) {
            throw new IllegalStateException("ApplicationEventMulticaster not initialized - " +
                    "call 'refresh' before multicasting events via the context: " + this);
        }
        return this.applicationEventMulticaster;
    }

    /**
     * Return the internal LifecycleProcessor used by the context.
     * @return the internal LifecycleProcessor (never {@code null})
     * @throws IllegalStateException if the context has not been initialized yet
     */
    LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
        if (this.lifecycleProcessor == null) {
            throw new IllegalStateException("LifecycleProcessor not initialized - " +
                    "call 'refresh' before invoking lifecycle methods via the context: " + this);
        }
        return this.lifecycleProcessor;
    }

    /**
     * Return the ResourcePatternResolver to use for resolving location patterns
     * into Resource instances. Default is a
     * {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver},
     * supporting Ant-style location patterns.
     * <p>Can be overridden in subclasses, for extended resolution strategies,
     * for example in a web environment.
     * <p><b>Do not call this when needing to resolve a location pattern.</b>
     * Call the context's {@code getResources} method instead, which
     * will delegate to the ResourcePatternResolver.
     * @return the ResourcePatternResolver for this context
     * @see #getResources
     * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
     */
    protected ResourcePatternResolver getResourcePatternResolver() {
        return new PathMatchingResourcePatternResolver(this);
    }

    /**
     * Set the parent of this application context.
     * <p>The parent {@linkplain ApplicationContext#getEnvironment() environment} is
     * {@linkplain ConfigurableEnvironment#merge(ConfigurableEnvironment) merged} with
     * this (child) application context environment if the parent is non-{@code null} and
     * its environment is an instance of {@link ConfigurableEnvironment}.
     * @see ConfigurableEnvironment#merge(ConfigurableEnvironment)
     */
    @Override
    public void setParent(@Nullable ApplicationContext parent) {
        this.parent = parent;
        if (parent != null) {
            Environment parentEnvironment = parent.getEnvironment();
            if (parentEnvironment instanceof ConfigurableEnvironment) {
                getEnvironment().merge((ConfigurableEnvironment) parentEnvironment);
            }
        }
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    /**
     * Return the list of BeanFactoryPostProcessors that will get applied
     * to the internal BeanFactory.
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        Assert.notNull(listener, "ApplicationListener must not be null");
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.addApplicationListener(listener);
        }
        this.applicationListeners.add(listener);
    }

    public Collection<ApplicationListener<?>> getApplicationListeners() {
        return this.applicationListeners;
    }

    public void refresh() {
        synchronized (this.startupShutdownMonitor) {
            // 准备刷新当前上下文
            prepareRefresh();

            // Tell the subclass to refresh the internal bean factory.
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

            // prepare the bean factory for use in this context.
            prepareBeanFactory(beanFactory);   // 为当前使用的上下文 准备bean工厂

           try {
               // Allows post-processing of the bean factory in context subclasses.
               postProcessBeanFactory(beanFactory); // 允许在上下文子类中对bean工厂进行后置处理。

               // Invoke factory processors registered as beans in the context.
               invokeBeanFactoryPostProcessors(beanFactory);  // 调用上下文中注册为bean的工厂处理器。

               // Register bean processors that intercept bean creation.
               registerBeanPostProcessors(beanFactory); // 注册拦截bean创建的bean处理器

               // Initialize message source for this context
               initMessageSource();  // 为当前上下文初始化 消息

               // Initialize event multicaster for this context.
               initApplicationEventMulticaster();  // 为当前上下文初始化事件多播器

               // Initialize other special beans in specific context subclasses
               onRefresh();    // 在特定上下文子类中 初始化其他特定bean

               // Check for listener beans and register them.
               registerListeners();  // 检查和注册 监听beans

               // Instantiate all remaining (non-lazy-init) singletons.
               finishBeanFactoryInitialization(beanFactory);  // 实例化所有剩余的(非懒加载初始化)单例。

               // Last step: publish corresponding event.
               finishRefresh(); // 最后一步， 发布相应的事件
           }
           catch (BeansException ex) {

               // Destroy already created singletons to avoid dangling resources
               destroyBeans();  // 销毁已经创建的单例以避免挂起资源

               // Reset active flag. 重新激活
               cancelRefresh(ex);
           }
           finally {
               // Rest common introspection caches in Spring's core, since we
               // might not ever need metadata for singleton beans naymore...
               resetCommonCaches();
           }

        }
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
    }


    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){

    }

    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }

    /**
     * 刷新前的预处理
     */
    protected void prepareRefresh() {
        // 开启时间
        this.startupDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);

        // Initialize any placeholder property sources in the context environment.
        initPropertySources();   // 初始化资源参数

        getEnvironment().validateRequiredProperties();

        if (this.earlyApplicationListeners == null) {
            this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
        }
        else {
            // 重置所有应用监听
            this.applicationListeners.clear();
            this.applicationListeners.addAll(this.earlyApplicationListeners);
        }

        // 允许收集早期的applicationevent，
        this.earlyApplicationEvents = new LinkedHashSet<>();

    }

    /**
     * 用实际实例替换任何存根属性源。
     */
    protected void initPropertySources() {
        // For subclasses: do nothing by default.
    }

    /**
     * Tell the subclass to refresh the internal bean factory
     * 告诉子类即将刷新内部bean工程
     * @return
     */
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    /**
     * Subclasses must implement this method to perform the actual configuration load.
     * 子类必须实现该方法 去执行实际的配置加载
     * The method is invoked by {@link #refresh()} before any other initialization work.
     * 在任何其他初始化工作之前 该方法被 refresh 调用
     * A subclass will either create a new bean factory and hold a reference to it,
     * or return a single BeanFactory instance that it holds.
     * 一个子类将要么创建一个新的bean工厂并且持有对它的引用， 要么return 它持有的一个单个 bean工厂实例
     * usually throw an IllegalStateException if refreshing the context more than once.
     * 通常情况下如果上下文引用超过一个将抛出非法状态异常
     *
     * @throws BeansException
     * @throws IllegalStateException
     */
    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;


    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    /**
     * Configure the factory's standard context characteristics.
     * such as the context's ClassLoader and post-processors.
     * bean工厂标准的上下文字符集配置
     * 例如上下文的 类加载器和后置处理器
     * @param beanFactory
     */
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // Tell the internal bean factory to use the context's class loader etc.
        // 设置bean类加载器
        beanFactory.setBeanClassLoader(getClassLoader());
        // 设置bean表达式解析器
        beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
        // 属性设置注册
        beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

        // configure the bean factory with context callbacks
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 忽略依赖接口
        beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
        beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
        beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
        beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

        // BeanFactory interface not registered as resolvable type plain factory.
        // MessageSource registered (and found for autowiring) as a bean.
        beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
        beanFactory.registerResolvableDependency(ResourceLoader.class, this);
        beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
        beanFactory.registerResolvableDependency(ApplicationContext.class, this);

        // Register early post-processor for detecting inner beans as ApplicationListeners.
        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

        // Detect a LoadTimeWeaver and prepare for weaving, if found.
        if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
            beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
            // Set a temporary ClassLoader for type matching.
            beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
        }

        // Register default environment beans.
        if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
            beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
        }

        if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
            beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
        }
        if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
            beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
        }

    }










}
