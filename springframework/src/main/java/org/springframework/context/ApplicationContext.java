package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
                                        MessageSource, ApplicationEventPublisher, ResourcePatternResolver {


    @Nullable
    String getId();


    String getApplicationName();


    String getDisplayName();


    long getStartupDate();

    @Nullable
    ApplicationContext getParent();



}
