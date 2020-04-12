package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

public class FactoryBeanNotInitializedException extends FatalBeanException {
    public FactoryBeanNotInitializedException(String msg) {
        super(msg);
    }
}
