package org.springframework.expression;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:49
 */
public interface ParserContext {

    boolean isTemplate();

    String getExpressionPrefix();

    String getExpressionSuffix();
}
