package org.drachens.fileManagement.filetypes;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class YamlFileType {
    private final String identifier;
    private final YamlConfigurationLoader configurationLoader;
    private ConfigurationNode c;

    public YamlFileType(String name, String path) {
        this.identifier = name;
        File file = new File(path);
        fileExists(file);
        configurationLoader = YamlConfigurationLoader.builder()
                .file(file)
                .build();
        try {
            c = configurationLoader.load();
        } catch (ConfigurateException e) {
            System.err.println("Unable to load file " + name + e.getMessage());
        }
    }

    private void fileExists(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                //don't need anything here
            }
        }
    }

    public void save() {
        try {
            configurationLoader.save(c);
        } catch (ConfigurateException e) {
            System.err.println("Error saving " + identifier + " " + e.getMessage());
        }
    }

    public ConfigurationNode getConfigurationNode() {
        return c;
    }

    protected void addDefault(Object data, Object... path) throws SerializationException {
        if (c.node(path).isNull()) {
            c.node(path).set(data);
        }
    }

    protected void set(Object data, Object... path) {
        try {
            c.node(path).set(data);
        } catch (SerializationException e) {
            System.err.println("Error setting data " + e.getMessage());
        }

    }

    protected <T> void addToList(T data, Class<T> clazz, Object... path) {
        ConfigurationNode newNode = c.node(path);
        try {
            List<T> list = newNode.getList(clazz);
            if (list == null) {
                list = new ArrayList<>();
            }

            if (!list.contains(data)) {
                list.add(data);
                newNode.setList(clazz, list);
                save();
            }
        } catch (SerializationException e) {
            System.err.println("Error adding " + data + " " + e.getMessage());
        }
    }

    protected <T> void removeFromList(T data, Class<T> clazz, Object... path) {
        ConfigurationNode newNode = c.node(path);
        try {
            List<T> list = newNode.getList(clazz);
            if (list == null) {
                list = new ArrayList<>();
            }

            list.remove(data);
            newNode.setList(clazz, list);
            save();
        } catch (SerializationException e) {
            System.err.println("Error adding " + data + " " + e.getMessage());
        }
    }

    protected <T> List<T> getFromList(Class<T> clazz, Object... path) {
        try {
            return c.node(path).getList(clazz);
        } catch (SerializationException e) {
            System.err.println("Error getting from list " + e.getMessage());
            return new ArrayList<>();
        }
    }

    protected abstract void initialLoad();

    protected abstract void setDefaults();
}
