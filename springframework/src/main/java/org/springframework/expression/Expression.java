package org.springframework.expression;

import com.sun.tools.corba.se.idl.constExpr.EvaluationException;
import org.springframework.lang.Nullable;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 上午10:51
 */
public interface Expression {

    @Nullable
    Object getValue() throws EvaluationException;

    @Nullable
    <T> T getValue(@Nullable Class<T> desiredResultType) throws EvaluationException;

    @Nullable
    Object getValue(EvaluationContext context) throws EvaluationException;
}
