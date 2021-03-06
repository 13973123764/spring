package org.springframework.context;

import java.util.Locale;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午3:59
 */
public class NoSuchMessageException extends RuntimeException {

    /**
     * Create a new exception.
     * @param code code that could not be resolved for given locale
     * @param locale locale that was used to search for the code within
     */
    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    /**
     * Create a new exception.
     * @param code code that could not be resolved for given locale
     */
    public NoSuchMessageException(String code) {
        super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
    }

}
