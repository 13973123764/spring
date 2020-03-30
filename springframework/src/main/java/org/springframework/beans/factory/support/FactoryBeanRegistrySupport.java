package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * Support base class for singleton registries which need to handle
 * 支持单例注册器中需要处理的基类
 * @author ZhouFan
 * @Date 2020/03/29 上午9:03
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    /**
     * Return the security context for this bean factory. If a security manager
     * is set, interaction with the user code will be executed using the privileged
     * of the security context returned by this method.
     * @see AccessController#getContext()
     */
    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }

}
