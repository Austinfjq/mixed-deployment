package cn.harmonycloud.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

public class LoadConfig {
    public static void load(Class<?> configClass, String file) {
        try {
            Properties props = new Properties();
//            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(file)) {
            System.out.println("loadfile:"+file);
            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                props.load(inputStream);
            }catch (Exception e){
                throw new RuntimeException("Error loading file: " + e, e);
            }
            for (Field field : configClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    if ("LOGGER".equals(field.getName())) {
                        continue;
                    }
                    field.set(null, getValue(props, field.getName(), field.getType()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: " + e, e);
        }
    }

    private static Object getValue(Properties props, String name, Class<?> type) {
        String value = props.getProperty(name);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration value: " + name);
        }
        if (type == String.class) {
            return value;
        }
        if (type == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type == int.class) {
            return Integer.parseInt(value);
        }
        if (type == float.class) {
            return Float.parseFloat(value);
        }
        if (type == double.class) {
            return Double.parseDouble(value);
        }
        throw new IllegalArgumentException("Unknown configuration value type: " + type.getName());
    }
}

