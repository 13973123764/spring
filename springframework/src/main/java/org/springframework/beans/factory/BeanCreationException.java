package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BeanCreationException extends FatalBeanException {

    @Nullable
    private String resourceDescription;

    @Nullable
    private List<Throwable> relatedCauses;

    @Nullable
    private String beanName;

    public BeanCreationException(String msg) {
        super(msg);
    }

    public BeanCreationException(String beanName, String msg) {
        super("Error creating bean with name '" + beanName + "': " + msg);
        this.beanName = beanName;
    }

    public BeanCreationException(String beanName, String msg, Throwable cause) {
        this(beanName, msg);
        initCause(cause);
    }

    public BeanCreationException(@Nullable String resourceDescription, @Nullable String beanName, String msg) {
        super("Error creating bean with name '" + beanName + "'" +
                (resourceDescription != null ? " defined in " + resourceDescription : "") + ": " + msg);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
    }

    public BeanCreationException(@Nullable String resourceDescription, String beanName, String msg, Throwable cause) {
        this(resourceDescription, beanName, msg);
        initCause(cause);
    }

    public void addRelatedCause(Throwable ex) {
        if (this.relatedCauses == null) {
            this.relatedCauses = new ArrayList<>();
        }
        this.relatedCauses.add(ex);
    }

}
