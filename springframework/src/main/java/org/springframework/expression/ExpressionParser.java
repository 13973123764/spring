package org.springframework.expression;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:48
 */
public interface ExpressionParser {

    Expression parseExpression(String expressionString, ParserContext context) throws ParseException;
}
