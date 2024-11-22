package org.drachens.util;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;

public class YamlUtil {
    public static void addToList(ConfigurationNode configurationNode, String s) {
        try {
            List<String> list = configurationNode.getList(String.class);
            if (list == null) {
                list = new ArrayList<>();
            }

            if (!list.contains(s)) {
                list.add(s);
            }

            configurationNode.setList(String.class, list);
        } catch (SerializationException e) {
            System.err.println("Error adding " + s + " " + e.getMessage());
        }
    }

    public static void removeFromList(ConfigurationNode configurationNode, String s) {
        try {
            List<String> list = configurationNode.getList(String.class);
            if (list == null) {
                list = new ArrayList<>();
            }

            list.remove(s);

            configurationNode.setList(String.class, list);
        } catch (SerializationException e) {
            System.err.println("Error adding " + s + " " + e.getMessage());
        }
    }

    public static List<String> loadFromList(ConfigurationNode configurationNode) {
        try {
            return configurationNode.getList(String.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
