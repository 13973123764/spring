package org.springframework.context.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if(definition instanceof AnnotatedBeanDefinitionReader){
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                return beanName;
            }
        }
        return null;
    }


    /**
     * derive bean name from the one of the annotations on the class.
     * 从其中一个注释中派生出一个 bean 名称
     * @param annotatedDef bean
     * @return 返回bean名称 或者 null(如果一个都没有找到)
     */
    @Nullable
    public String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef){
        AnnotationMetadata amd = annotatedDef.getMetadata();
        Set<String> types = amd.getAnnotationTypes();
        String beanName =null;
        for (String type : types) {
            AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(amd, type);
            if (attributes != null && isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), attributes)) {
                Object value = attributes.get("value");
                if (value instanceof String) {
                    String strVal = (String) value;
                    if (StringUtils.hasLength(strVal)) {
                        if (beanName != null && !strVal.equals(beanName)) {
                            throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
                                    "component names: '" + beanName + "' versus '" + strVal + "'");
                        }
                        beanName = strVal;
                    }
                }
            }
        }
        return beanName;
    }


    /**
     * Check whether the given annotation is a stereotype that is allowed
     * to suggest a component name through its annotation {@code value()}.
     * 检查给定注解是否是一个允许通过其注释建议的组件名注解
     * @param annotationType        the name of annotation class to check 要检查的注解类名
     * @param metaAnnotationTypes   the name of meta-annotations class to
     * @param attributes
     * @return
     */
    protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes,
                                                @Nullable Map<String, Object> attributes){
        boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) ||
                metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME) ||
                annotationType.equals("javax.annotation.ManagedBean") ||
                annotationType.equals("javax.inject.Named");
        return (isStereotype && attributes != null && attributes.containsKey("value"));
    }


    /**
     * Derive a default bean name from the given bean definition.
     * 从给定的bean派生出一个默认的bean名称
     * @param definition
     * @param registry
     * @return
     */
    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry){
        return buildDefaultBeanName(definition);
    }

    /**
     * derive a default bean name from the given bean definition
     * 从给定的bean定义中 派生出一个默认的bean名称
     * @param definition
     * @return
     */
    protected String buildDefaultBeanName(BeanDefinition definition){
        String beanClassName = definition.getBeanClassName();

        return null;
    }











}