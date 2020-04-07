package org.springframework.core;

import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.jvm.ReflectJvmMapping;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 上午11:10
 */
public class MethodParameter {

    private final Executable executable;

    private final int parameterIndex;

    private int nestingLevel;

    @Nullable
    private volatile Parameter parameter;

    @Nullable
    private volatile Class<?> containingClass;

    @Nullable
    private volatile Class<?> parameterType;

    @Nullable
    private volatile Type genericParameterType;

    /** Map from Integer level to Integer type index. */
    @Nullable
    Map<Integer, Integer> typeIndexesPerLevel;

    @Nullable
    private volatile Annotation[] parameterAnnotations;

    @Nullable
    private volatile ParameterNameDiscoverer parameterNameDiscoverer;

    @Nullable
    private volatile String parameterName;

    @Nullable
    private volatile MethodParameter nestedMethodParameter;

    /**
     * Create a new {@code MethodParameter} for the given method, with nesting level 1.
     * @param method the Method to specify a parameter for
     * @param parameterIndex the index of the parameter: -1 for the method
     * return type; 0 for the first method parameter; 1 for the second method
     * parameter, etc.
     */
    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    /**
     * Return the wrapped Constructor, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Constructor, or {@code null} if none
     */
    @Nullable
    public Constructor<?> getConstructor() {
        return (this.executable instanceof Constructor ? (Constructor<?>) this.executable : null);
    }

    /**
     * Create a new {@code MethodParameter} for the given method.
     * @param method the Method to specify a parameter for
     * @param parameterIndex the index of the parameter: -1 for the method
     * return type; 0 for the first method parameter; 1 for the second method
     * parameter, etc.
     * @param nestingLevel the nesting level of the target type
     * (typically 1; e.g. in case of a List of Lists, 1 would indicate the
     * nested List, whereas 2 would indicate the element of the nested List)
     */
    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        Assert.notNull(method, "Method must not be null");
        this.executable = method;
        this.parameterIndex = validateIndex(method, parameterIndex);
        this.nestingLevel = nestingLevel;
    }

    /**
     * Create a new MethodParameter for the given constructor, with nesting level 1.
     * @param constructor the Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     */
    public MethodParameter(Constructor<?> constructor, int parameterIndex) {
        this(constructor, parameterIndex, 1);
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        Assert.notNull(constructor, "Constructor must not be null");
        this.executable = constructor;
        this.parameterIndex = validateIndex(constructor, parameterIndex);
        this.nestingLevel = nestingLevel;
    }

    /**
     * Internal constructor used to create a {@link MethodParameter} with a
     * containing class already set.
     * @param executable the Executable to specify a parameter for
     * @param parameterIndex the index of the parameter
     * @param containingClass the containing class
     * @since 5.2
     */
    MethodParameter(Executable executable, int parameterIndex, @Nullable Class<?> containingClass) {
        Assert.notNull(executable, "Executable must not be null");
        this.executable = executable;
        this.parameterIndex = validateIndex(executable, parameterIndex);
        this.nestingLevel = 1;
        this.containingClass = containingClass;
    }

    /**
     * Copy constructor, resulting in an independent MethodParameter object
     * based on the same metadata and cache state that the original object was in.
     * @param original the original MethodParameter object to copy from
     */
    public MethodParameter(MethodParameter original) {
        Assert.notNull(original, "Original must not be null");
        this.executable = original.executable;
        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
    }

    private static int validateIndex(Executable executable, int parameterIndex) {
        int count = executable.getParameterCount();
        Assert.isTrue(parameterIndex >= -1 && parameterIndex < count,
                () -> "Parameter index needs to be between -1 and " + (count - 1));
        return parameterIndex;
    }

    /**
     * Return the containing class for this method parameter.
     * @return a specific containing class (potentially a subclass of the
     * declaring class), or otherwise simply the declaring class itself
     * @see #getDeclaringClass()
     */
    public Class<?> getContainingClass() {
        Class<?> containingClass = this.containingClass;
        return (containingClass != null ? containingClass : getDeclaringClass());
    }

    /**
     * Return the class that declares the underlying Method or Constructor.
     */
    public Class<?> getDeclaringClass() {
        return this.executable.getDeclaringClass();
    }

    public int getNestingLevel() {
        return this.nestingLevel;
    }

    /**
     * Return the wrapped Method, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Method, or {@code null} if none
     */
    @Nullable
    public Method getMethod() {
        return (this.executable instanceof Method ? (Method) this.executable : null);
    }

    /**
     * Return the wrapped executable.
     * @return the Method or Constructor as Executable
     * @since 5.0
     */
    public Executable getExecutable() {
        return this.executable;
    }

    /**
     * Return the index of the method/constructor parameter.
     * @return the parameter index (-1 in case of the return type)
     */
    public int getParameterIndex() {
        return this.parameterIndex;
    }

    /**
     * Return the generic type of the method/constructor parameter.
     * @return the parameter type (never {@code null})
     * @since 3.0
     */
    public Type getGenericParameterType() {
        Type paramType = this.genericParameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = (method != null ?
                        (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(getContainingClass()) ?
                                KotlinDelegate.getGenericReturnType(method) : method.getGenericReturnType()) : void.class);
            }
            else {
                Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
                int index = this.parameterIndex;
                if (this.executable instanceof Constructor &&
                        ClassUtils.isInnerClass(this.executable.getDeclaringClass()) &&
                        genericParameterTypes.length == this.executable.getParameterCount() - 1) {
                    // Bug in javac: type array excludes enclosing instance parameter
                    // for inner classes with at least one generic constructor parameter,
                    // so access it with the actual parameter index lowered by 1
                    index = this.parameterIndex - 1;
                }
                paramType = (index >= 0 && index < genericParameterTypes.length ?
                        genericParameterTypes[index] : computeParameterType());
            }
            this.genericParameterType = paramType;
        }
        return paramType;
    }

    private Class<?> computeParameterType() {
        if (this.parameterIndex < 0) {
            Method method = getMethod();
            if (method == null) {
                return void.class;
            }
            if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(getContainingClass())) {
                return KotlinDelegate.getReturnType(method);
            }
            return method.getReturnType();
        }
        return this.executable.getParameterTypes()[this.parameterIndex];
    }



    /**
     * Inner class to avoid a hard dependency on Kotlin at runtime.
     */
    private static class KotlinDelegate {

        /**
         * Check whether the specified {@link MethodParameter} represents a nullable Kotlin type
         * or an optional parameter (with a default value in the Kotlin declaration).
         */
        public static boolean isOptional(MethodParameter param) {
            Method method = param.getMethod();
            Constructor<?> ctor = param.getConstructor();
            int index = param.getParameterIndex();
            if (method != null && index == -1) {
                KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
                return (function != null && function.getReturnType().isMarkedNullable());
            }
            else {
                KFunction<?> function = null;
                Predicate<KParameter> predicate = null;
                if (method != null) {
                    function = ReflectJvmMapping.getKotlinFunction(method);
                    predicate = p -> KParameter.Kind.VALUE.equals(p.getKind());
                }
                else if (ctor != null) {
                    function = ReflectJvmMapping.getKotlinFunction(ctor);
                    predicate = p -> KParameter.Kind.VALUE.equals(p.getKind()) ||
                            KParameter.Kind.INSTANCE.equals(p.getKind());
                }
                if (function != null) {
                    List<KParameter> parameters = function.getParameters();
                    KParameter parameter = parameters
                            .stream()
                            .filter(predicate)
                            .collect(Collectors.toList())
                            .get(index);
                    return (parameter.getType().isMarkedNullable() || parameter.isOptional());
                }
            }
            return false;
        }

        /**
         * Return the generic return type of the method, with support of suspending
         * functions via Kotlin reflection.
         */
        static private Type getGenericReturnType(Method method) {
            KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
            if (function != null && function.isSuspend()) {
                return ReflectJvmMapping.getJavaType(function.getReturnType());
            }
            return method.getGenericReturnType();
        }

        /**
         * Return the return type of the method, with support of suspending
         * functions via Kotlin reflection.
         */
        static private Class<?> getReturnType(Method method) {
            KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
            if (function != null && function.isSuspend()) {
                Type paramType = ReflectJvmMapping.getJavaType(function.getReturnType());
                return ResolvableType.forType(paramType).resolve(method.getReturnType());
            }
            return method.getReturnType();
        }
    }
}
