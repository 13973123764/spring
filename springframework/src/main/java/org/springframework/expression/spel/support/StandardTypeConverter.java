package org.springframework.expression.spel.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.expression.TypeConverter;
import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午11:16
 */
public class StandardTypeConverter implements TypeConverter {

    private final ConversionService conversionService;

    public StandardTypeConverter(ConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.conversionService = conversionService;
    }
}
