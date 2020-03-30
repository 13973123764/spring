package org.springframework.core.type.filter;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:14
 */
public abstract class AbstractTypeHierarchyTraversingFilter implements TypeFilter{

    private final boolean considerInherited;

    private final boolean considerInterfaces;

    protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces) {
        this.considerInherited = considerInherited;
        this.considerInterfaces = considerInterfaces;
    }

}
