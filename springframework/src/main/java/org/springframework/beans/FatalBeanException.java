package org.springframework.beans;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午4:57
 */
public class FatalBeanException extends BeansException  {

    public FatalBeanException(String msg) {
        super(msg);
    }
}
