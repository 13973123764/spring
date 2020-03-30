package org.springframework.beans;

import org.springframework.lang.Nullable;

/**
 * Interface to be implemented by bean metadata elements
 * that carry a configuration source object
 */
public interface BeanMetadataElement {

    @Nullable
    Object getSource();

}
