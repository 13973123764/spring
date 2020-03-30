package org.springframework.beans.factory;

/**
 * The boot interface for accessing a spring bean container.
 * This is the basic client view of a bean container.
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * 用户访问bean容器的主接口
 * bean容器的基本客户端视图
 * 更进一步接口 比如 listableBeanFactory 和 ConfigurableBeanFactory
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is same;
 *
 * 当前接口是由包含多个bean定义的对象实现的，字符串名称都是每一个唯一的标识符，取决于bean的定义
 * 该工厂将返回 一个独立的包含对象的实例(原型设计模式) 或者 单个共享的实例(单例模式), 返回那种类型
 * 的实例取决于bean工厂的配置（API是相同的）
 *
 *
 */
public interface BeanFactory {
}
