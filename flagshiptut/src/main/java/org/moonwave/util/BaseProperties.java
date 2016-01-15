package org.moonwave.util;

import java.util.Properties;

public abstract class BaseProperties {

    protected Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperties(String key, String value) {
        properties.setProperty(key, value);
    }
}
