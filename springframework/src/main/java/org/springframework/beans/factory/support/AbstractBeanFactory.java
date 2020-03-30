package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.lang.Nullable;

import java.security.AccessControlContext;
import java.security.AccessController;

/**
 *
 * @author ZhouFan
 * @Date 2020/03/29 上午9:02
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /** Security context used when running with a SecurityManager. */
    @Nullable
    private SecurityContextProvider securityContextProvider;

    @Override
    public AccessControlContext getAccessControlContext() {
        return (this.securityContextProvider != null ?
                this.securityContextProvider.getAccessControlContext() :
                AccessController.getContext());
    }
}
