package org.springframework.expression.spel.standard;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.util.Assert;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午12:16
 */
public class SpelExpressionParser extends TemplateAwareExpressionParser {

    private final SpelParserConfiguration configuration;

    @Override
    public Expression parseExpression(String expressionString, ParserContext context) throws ParseException {
        return null;
    }

    public SpelExpressionParser(SpelParserConfiguration configuration) {
        Assert.notNull(configuration, "SpelParserConfiguration must not be null");
        this.configuration = configuration;
    }
}
