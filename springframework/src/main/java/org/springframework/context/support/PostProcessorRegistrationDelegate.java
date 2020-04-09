package org.springframework.context.support;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午5:44
 */
final class PostProcessorRegistrationDelegate {


    /**
     * 注册bean 后置处理器
     * @param beanFactory           bean工厂
     * @param applicationContext    应用上下文
     */
    public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory,
                                                  AbstractApplicationContext applicationContext){
        // 获取bean名称类型
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
        // 在bean后置处理器初始化期间 一个bean对象创建完毕
        int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
        // 添加bean后置处理器
        beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

        // 排序后的优先级置处理器
        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        // 内部后置处理器
        List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
        // 排序后的后置处理器名称
        List<String> orderedPostProcessorNames = new ArrayList<>();
        // 不排序后置处理器名称
        List<String> nonOrderedPostProcessorNames = new ArrayList<>();

        // 遍历后置处理器名称
        for (String ppName : postProcessorNames) {
            // 判断类型是否匹配优先级排序类型
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                // 获取bean后置处理器对象
                BeanPostProcessor pp = beanFactoryAb.getBean(ppName, BeanPostProcessor.class);
                // 添加到内部后置处理器
                if (pp instanceof MergedBeanDefinitionPostProcessor) {
                    internalPostProcessors.add(pp);
                }
            }
            // 判断是否匹配排序类型
            else if (beanFactory.isTypeMatch(ppName, Ordered.class)){
                // 添加到排序后置处理器名称集合中
                orderedPostProcessorNames.add(ppName);
            }
            else {
                // 其他保存在 不需要排序后置处理中名称集合中
                nonOrderedPostProcessorNames.add(ppName);
            }
        }

        // 第一步,注册实现了优先级的 bean后置处理器
        sortPostProcessor(priorityOrderedPostProcessors, beanFactory);
        registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

        // 下一步, 注册实现了排序的bean后置处理器
        List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
        // 遍历排序后置处理器名称
        for (String ppName : orderedPostProcessorNames) {
            // 获取后置处理器名称
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            orderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);

            }
        }


    }


    /**
     * 排序
     * @param postProcessor
     * @param beanFactory
     */
    private static void sortPostProcessor(List<?> postProcessor, ConfigurableListableBeanFactory beanFactory){
        Comparator<Object> comparatorToUse = null;
        if (beanFactory instanceof DefaultListableBeanFactory) {
            comparatorToUse =((DefaultListableBeanFactory) beanFactory).getDependecyComparator();
        }
        if (comparatorToUse == null) {
            comparatorToUse = OrderComparator.INSTANCE;
        }
        postProcessor.sort(comparatorToUse);
    }


    /**
     * 注册bean工厂
     * @param beanFactory
     * @param postProcessors
     */
    private static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors){
        for (BeanPostProcessor postProcessor : postProcessors) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }



    /**
     * 通过所有bean后置处理器获得处理器
     */
    private static final class BeanPostProcessorChecker implements BeanPostProcessor{

        private final ConfigurableListableBeanFactory beanFactory;

        private final int beanPostProcessorTargetCount;

        public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
            this.beanFactory = beanFactory;
            this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
        }



    }












}
