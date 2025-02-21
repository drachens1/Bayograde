package org.drachens.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum JsonUtil {
    ;

    public static <T> void addToList(T value, Class<T> clazz, JsonObject root, String... path) {
        JsonObject parent = getOrCreateParent(root, path);
        String key = path[path.length - 1];

        JsonArray array = parent.has(key) ? parent.getAsJsonArray(key) : new JsonArray();
        JsonElement jsonValue = new Gson().toJsonTree(value, clazz);

        if (!array.contains(jsonValue)) {
            array.add(jsonValue);
            parent.add(key, array);
        }
    }

    public static <T> void removeFromList(T value, Class<T> clazz, JsonObject root, String... path) {
        JsonObject parent = getOrCreateParent(root, path);
        String key = path[path.length - 1];

        if (!parent.has(key)) return;

        JsonArray array = parent.getAsJsonArray(key);
        JsonElement jsonValue = new Gson().toJsonTree(value, clazz);

        array.remove(jsonValue);
        parent.add(key, array);
    }

    public static <T> List<T> getFromList(Class<T> clazz, JsonObject root, String... path) {
        JsonObject parent = getOrCreateParent(root, path);
        String key = path[path.length - 1];

        List<T> list = new ArrayList<>();

        if (!parent.has(key)) return list;

        JsonElement element = parent.get(key);

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (JsonElement jsonElement : array) {
                list.add(new Gson().fromJson(jsonElement, clazz));
            }
        } else if (element.isJsonPrimitive()) {
            list.add(new Gson().fromJson(element, clazz));
        } else {
            System.err.println("Error: " + key);
        }

        return list;
    }

    public static <T> HashMap<String, T> loadHashMap(Class<T> clazz, JsonObject root, String... path) {
        JsonObject parent = getOrCreateParent(root, path);
        String key = path[path.length - 1];

        HashMap<String, T> map = new HashMap<>();

        if (!parent.has(key) || !parent.get(key).isJsonObject()) {
            return map;
        }

        JsonObject jsonObject = parent.getAsJsonObject(key);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), new Gson().fromJson(entry.getValue(), clazz));
        }

        return map;
    }

    public static <T> void saveHashMap(HashMap<String, T> map, Class<T> clazz, JsonObject root, String... path) {
        JsonObject parent = getOrCreateParent(root, path);
        String key = path[path.length - 1];

        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            jsonObject.add(entry.getKey(), new Gson().toJsonTree(entry.getValue(), clazz));
        }

        parent.add(key, jsonObject);
    }

    private static JsonObject getOrCreateParent(JsonObject root, String... path) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            if (!current.has(path[i]) || !current.get(path[i]).isJsonObject()) {
                current.add(path[i], new JsonObject());
            }
            current = current.getAsJsonObject(path[i]);
        }
        return current;
    }
}
