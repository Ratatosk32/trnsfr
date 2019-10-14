package com.domain.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Helper class provides basic property manipulation methods. */
public final class PropertyUtils {

    private static Properties properties = new Properties();

    public static String getStringProperty(String key) {
        String value = properties.getProperty(key);
        return value == null ? System.getProperty(key) : value;
    }

    static {
        try {
            InputStream fis =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
