package org.springframework.core.env;

import org.springframework.lang.Nullable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午10:29
 */
public interface PropertySources extends Iterable<PropertySource<?>> {

    default Stream<PropertySource<?>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    boolean contains(String name);

    @Nullable
    PropertySource<?> get(String name);

}
