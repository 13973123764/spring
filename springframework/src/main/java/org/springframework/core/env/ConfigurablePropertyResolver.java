package org.springframework.core.env;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/28 下午11:37
 */
public interface ConfigurablePropertyResolver extends PropertyResolver{

    void validateRequiredProperties() throws MissingRequiredPropertiesException;

}
