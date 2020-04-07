package org.springframework.core.env;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午10:29
 */
public abstract class PropertySource<T> {

    protected final String name;

    protected final T source;

    public PropertySource(String name, T source) {
        Assert.hasText(name, "Property source name must contain at least one character");
        Assert.notNull(source, "Property source must not be null");
        this.name = name;
        this.source = source;
    }

    public PropertySource(String name) {
        this(name, (T) new Object());
    }

    public String getName() {
        return this.name;
    }

    public T getSource() {
        return this.source;
    }

    public boolean containsProperty(String name) {
        return (getProperty(name) != null);
    }

    @Nullable
    public abstract Object getProperty(String name);

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof PropertySource &&
                ObjectUtils.nullSafeEquals(this.name, ((PropertySource<?>) other).name)));
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.name);
    }

    public static PropertySource<?> named(String name) {
        return new ComparisonPropertySource(name);
    }

    public static class StubPropertySource extends PropertySource<Object> {

        public StubPropertySource(String name) {
            super(name, new Object());
        }

        /**
         * Always returns {@code null}.
         */
        @Override
        @Nullable
        public String getProperty(String name) {
            return null;
        }
    }


    static class ComparisonPropertySource extends StubPropertySource {

        private static final String USAGE_ERROR =
                "ComparisonPropertySource instances are for use with collection comparison only";

        public ComparisonPropertySource(String name) {
            super(name);
        }

        @Override
        public Object getSource() {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }

        @Override
        public boolean containsProperty(String name) {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }

        @Override
        @Nullable
        public String getProperty(String name) {
            throw new UnsupportedOperationException(USAGE_ERROR);
        }
    }

}
