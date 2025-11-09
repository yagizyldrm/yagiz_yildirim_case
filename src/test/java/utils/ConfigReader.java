package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration reader for test settings.
 * Reads from config.properties with fallback to system properties and environment variables.
 * Priority order:
 * 1. config.properties file
 * 2. System property (-Dkey=value)
 * 3. Environment variable (KEY_NAME)
 * 4. Default value
 */
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    
    static {
        loadProperties();
    }
    
    private ConfigReader() {}
    
    /**
     * Load properties from config.properties file in classpath.
     */
    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if(input == null){
                System.err.println("Warning: " + CONFIG_FILE + " not found in classpath. Using defaults.");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading " + CONFIG_FILE + ": " + e.getMessage());
        }
    }
    
    /**
     * Get property value with fallback mechanism.
     * 
     * @param key Property key
     * @param defaultValue Default value if not found
     * @return Property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        // 1) Try config.properties
        String value = properties.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        
        // 2) Try system property
        value = System.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        
        // 3) Try environment variable (replace . with _ and uppercase)
        String envKey = key.replace(".", "_").toUpperCase();
        value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        
        // 4) Return default
        return defaultValue;
    }
    
    /**
     * Get property value without default.
     * 
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Get boolean property value.
     * 
     * @param key Property key
     * @param defaultValue Default value
     * @return Boolean value
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Get integer property value.
     * 
     * @param key Property key
     * @param defaultValue Default value
     * @return Integer value
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value for " + key + ": " + value);
            return defaultValue;
        }
    }
}

