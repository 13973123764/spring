package org.springframework.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Support class for {@link AttributeAccessor AttributeAccessors}
 * providing a base implementation of all methods. To be extended by subclass
 * 提供所有方法的基本实现，由子类扩展
 */
public abstract class AttributeAccessorSupport implements AttributeAccessor, Serializable {


    private final Map<String, Object> attributes = new LinkedHashMap<>();


}
