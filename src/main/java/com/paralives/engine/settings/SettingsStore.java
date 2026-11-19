package com.paralives.engine.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsStore {
    private final Path path;
    private final Properties properties = new Properties();

    public SettingsStore(Path path) {
        this.path = path;
        load();
    }

    public static SettingsStore defaultStore() {
        return new SettingsStore(Paths.get(
                System.getProperty("user.home"),
                ".paralives",
                "settings.properties"));
    }

    private void load() {
        if (!Files.exists(path)) {
            return;
        }
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load settings: " + path, exception);
        }
    }

    public void save() {
        try {
            Files.createDirectories(path.getParent());
            try (OutputStream output = Files.newOutputStream(path)) {
                properties.store(output, "ParaLives settings");
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save settings: " + path, exception);
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defaultValue)));
    }

    public void setInt(String key, int value) {
        properties.setProperty(key, Integer.toString(value));
    }

    public void setBoolean(String key, boolean value) {
        properties.setProperty(key, Boolean.toString(value));
    }

    public Path getPath() {
        return path;
    }
}
