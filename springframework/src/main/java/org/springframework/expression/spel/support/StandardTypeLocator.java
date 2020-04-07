package org.springframework.expression.spel.support;

import org.springframework.expression.TypeLocator;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:15
 */
public class StandardTypeLocator implements TypeLocator {

    @Nullable
    private final ClassLoader classLoader;

    private final List<String> knownPackagePrefixes = new LinkedList<>();

    public StandardTypeLocator(@Nullable ClassLoader classLoader) {
        this.classLoader = classLoader;
        // Similar to when writing regular Java code, it only knows about java.lang by default
        registerImport("java.lang");
    }

    public void registerImport(String prefix) {
        this.knownPackagePrefixes.add(prefix);
    }
}
