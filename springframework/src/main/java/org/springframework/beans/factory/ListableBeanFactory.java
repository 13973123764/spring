package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Extension of the {@link BeanFactory} interface to be implemented by bean factories
 * that can enumerate all their bean instances, rather than attempting bean lookup
 * by name one by one as requested by clients. BeanFactory implementations that
 * preload all their bean definitions (such as XML-based factories) my implement
 * this interface
 * 由 bean 工厂实现的 beanFactory 接口的扩展
 * 它可以枚举它们的所有bean 实例，而不是按照客户机的请求逐个尝试按名称查找bean.
 * 预加载所有bean定义（例如基于XML的工厂）的BeanFactory实现可以实现这个接口
 *
 * If this is a {@link HierarchicalBeanFactory}, the return values will not
 * take any BeanFactory hierarchy into account, but will relate only to the beans
 * defined in the current factory. Use the {@link BeanFactoryUtils} helper class
 * to consider beans in ancestor factories too.
 * 如果这是HierarchicalBeanFactory，
 * 则返回值将不考虑任何BeanFactory层次结构，
 * 而只与当前工厂中定义的bean相关。
 * 使用BeanFactoryUtils助手类也可以考虑祖先工厂中的bean。
 *
 * The methods in this interface will just respect bean definitions of this factory.
 * They will ignore any singleton beans that have been registered by other means like
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}'s
 * {@code registerSingleton} method, with the exception of
 * {@code getBeanNamesOfType} and {@code getBeansOfType} which will check
 * such manually registered singletons too. Of course, BeanFactory's {@code getBean}
 * does allow transparent access to such special beans as well. However, in typical
 * scenarios, all beans will be defined by external bean definitions anyway, so most
 * applications don't need to worry about this differentiation.
 *
 * 这个接口中的方法将只考虑这个工厂的bean定义。他们会忽略任何通过其他方式注册的单例bean，比如...
 * 这种手工注册的单子也一样。当然，BeanFactory也允许透明地访问这些特殊的bean。然而，在典型的场景中，
 * 所有bean都将由外部bean定义来定义，因此大多数应用程序不需要担心这种差异。
 */
public interface ListableBeanFactory {


    /**
     * Check if this bean factory contains a bean definition with the given name.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     * 不考虑此工厂可能参与的任何层次结构，并忽略通过bean定义以外的其他方式注册的任何单例bean。
     * @param beanName the name of the bean to look for
     * @return if this bean factory contains a bean definition with the given name
     * @see #containsBean
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Return the number of beans defined in the factory.
     * 返回bean定义在工厂的数量
     * @return
     */
    int getBeanDefinitionCount();

    /**
     * Return the names of all beans defined in this factory.
     * 返回bean定义在工厂 所有的bean名称
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     *
     * @param type
     * @return
     */
    String[] getBeanNamesForType(ResolvableType type);

    String[] getBeanNamesForType(@Nullable Class<?> type);

    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);

    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;

    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;

    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);

    Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;

    @Nullable
    <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
            throws NoSuchBeanDefinitionException;
}
