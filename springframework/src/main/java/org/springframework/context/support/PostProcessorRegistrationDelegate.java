package org.springframework.context.support;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
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

        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
        List<String> orderedPostProcessorNames = new ArrayList<>();
        List<String> nonOrderedPostProcessorNames = new ArrayList<>();

        for (String ppName : postProcessorNames) {
            // 判断类型是否匹配优先级排序类型
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            }
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
