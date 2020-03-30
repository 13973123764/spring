package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

/**
 * Provider of the security context of the code running inside the bean factory.
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午12:07
 */
public interface SecurityContextProvider {

    /**
     * Provides a security access control context relevant to a bean factory.
     * @return bean factory security control context
     */
    AccessControlContext getAccessControlContext();
}
