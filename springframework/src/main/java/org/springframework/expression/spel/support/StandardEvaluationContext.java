package org.springframework.expression.spel.support;

import org.springframework.expression.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:57
 */
public class StandardEvaluationContext implements EvaluationContext {

    private TypedValue rootObject;

    @Nullable
    private volatile List<PropertyAccessor> propertyAccessors;

    @Nullable
    private BeanResolver beanResolver;

    @Nullable
    private TypeLocator typeLocator;

    @Nullable
    private TypeConverter typeConverter;


    public StandardEvaluationContext(@Nullable Object rootObject) {
        this.rootObject = new TypedValue(rootObject);
    }

    public void addPropertyAccessor(PropertyAccessor accessor) {
        addBeforeDefault(initPropertyAccessors(), accessor);
    }

    private static <T> void addBeforeDefault(List<T> resolvers, T resolver) {
        resolvers.add(resolvers.size() - 1, resolver);
    }

    private List<PropertyAccessor> initPropertyAccessors() {
        List<PropertyAccessor> accessors = this.propertyAccessors;
        if (accessors == null) {
            accessors = new ArrayList<>(5);
            accessors.add(new ReflectivePropertyAccessor());
            this.propertyAccessors = accessors;
        }
        return accessors;
    }

    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    public void setTypeLocator(TypeLocator typeLocator) {
        Assert.notNull(typeLocator, "TypeLocator must not be null");
        this.typeLocator = typeLocator;
    }

    public void setTypeConverter(TypeConverter typeConverter) {
        Assert.notNull(typeConverter, "TypeConverter must not be null");
        this.typeConverter = typeConverter;
    }

}
