package org.springframework.core;

import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/06 下午12:15
 */
public final class SpringProperties {

    private static final Properties localProperties = new Properties();


    @Nullable
    public static String getProperty(String key) {
        String value = localProperties.getProperty(key);
        if (value == null) {
            try {
                value = System.getProperty(key);
            }
            catch (Throwable ex) {
                System.out.println("Could not retrieve system property '" + key + "': " + ex);
            }
        }
        return value;
    }

}
