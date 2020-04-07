package org.springframework.core;

import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午2:04
 */
public class KotlinDetector {

    private static final boolean kotlinReflectPresent;

    @Nullable
    private static final Class<? extends Annotation> kotlinMetadata;

    /**
     * Determine whether Kotlin reflection is present.
     * @since 5.1
     */
    public static boolean isKotlinReflectPresent() {
        return kotlinReflectPresent;
    }

    static {
        Class<?> metadata;
        ClassLoader classLoader = KotlinDetector.class.getClassLoader();
        try {
            metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
        }
        catch (ClassNotFoundException ex) {
            // Kotlin API not available - no Kotlin support
            metadata = null;
        }
        kotlinMetadata = (Class<? extends Annotation>) metadata;
        kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
        if (kotlinMetadata != null && !kotlinReflectPresent) {
            System.out.println("Kotlin reflection implementation not found at runtime, related features won't be available.");
        }
    }

    /**
     * Determine whether the given {@code Class} is a Kotlin type
     * (with Kotlin metadata present on it).
     */
    public static boolean isKotlinType(Class<?> clazz) {
        return (kotlinMetadata != null && clazz.getDeclaredAnnotation(kotlinMetadata) != null);
    }


}
