package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Comparator;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:01
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {


    /** Optional OrderComparator for dependency Lists and arrays(为依赖的列表和数组的可选排序比较器) */
    @Nullable
    private Comparator<Object> dependencyComparator;

    private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();

    @Nullable
    public Comparator<Object> getDependencyComparator() {
        return dependencyComparator;
    }

    public AutowireCandidateResolver getAutowireCandidateResolver() {
        return this.autowireCandidateResolver;
    }

    public void setDependencyComparator(@Nullable Comparator<Object> dependencyComparator) {
        this.dependencyComparator = dependencyComparator;
    }

    /**
     * Return the dependency comparator for this BeanFactory
     * 返回这个bean工厂的 依赖比较器
     * @return
     */
    @Nullable
    public Comparator<Object> getDependecyComparator(){
        return this.dependencyComparator;
    }


    public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver){
        Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
        if (autowireCandidateResolver instanceof BeanFactoryAware) {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ((BeanFactoryAware) autowireCandidateResolver).setBeanFactory(DefaultListableBeanFactory.this);
                    return null;
                }, getAccessControlContext());
            }
            else {
                ((BeanFactoryAware) autowireCandidateResolver).setBeanFactory(this);
            }
        }
        this.autowireCandidateResolver = autowireCandidateResolver;
    }


    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {

    }
}
