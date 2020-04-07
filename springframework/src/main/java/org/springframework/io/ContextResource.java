package org.springframework.io;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:53
 */
public interface ContextResource {


    /**
     * Return the path within the enclosing 'context'.
     * <p>This is typically path relative to a context-specific root directory,
     * e.g. a ServletContext root or a PortletContext root.
     */
    String getPathWithinContext();
}
