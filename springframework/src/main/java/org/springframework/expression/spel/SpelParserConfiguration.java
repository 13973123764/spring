package org.springframework.expression.spel;

import org.springframework.core.SpringProperties;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午12:13
 */
public class SpelParserConfiguration {

    private static final SpelCompilerMode defaultCompilerMode;

    static {
        String compilerMode = SpringProperties.getProperty("spring.expression.compiler.mode");
        defaultCompilerMode = (compilerMode != null ?
                SpelCompilerMode.valueOf(compilerMode.toUpperCase()) : SpelCompilerMode.OFF);
    }

    private final SpelCompilerMode compilerMode;

    @Nullable
    private final ClassLoader compilerClassLoader;

    private final boolean autoGrowNullReferences;

    private final boolean autoGrowCollections;

    private final int maximumAutoGrowSize;

    public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader) {
        this(compilerMode, compilerClassLoader, false, false, Integer.MAX_VALUE);
    }

    public SpelParserConfiguration(@Nullable SpelCompilerMode compilerMode, @Nullable ClassLoader compilerClassLoader,
                                   boolean autoGrowNullReferences, boolean autoGrowCollections, int maximumAutoGrowSize) {

        this.compilerMode = (compilerMode != null ? compilerMode : defaultCompilerMode);
        this.compilerClassLoader = compilerClassLoader;
        this.autoGrowNullReferences = autoGrowNullReferences;
        this.autoGrowCollections = autoGrowCollections;
        this.maximumAutoGrowSize = maximumAutoGrowSize;
    }
}
