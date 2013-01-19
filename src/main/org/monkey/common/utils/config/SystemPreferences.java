package org.monkey.common.utils.config;

public interface SystemPreferences {
    String get(String key);
    Integer getInteger(String key, Integer defaultValue);
}
