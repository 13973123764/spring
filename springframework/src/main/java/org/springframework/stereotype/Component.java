package org.springframework.stereotype;

import java.lang.annotation.*;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
}
