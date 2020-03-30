package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午4:56
 */
public class BeanDefinitionStoreException extends FatalBeanException {

    public BeanDefinitionStoreException(String msg) {
        super(msg);
    }
}
