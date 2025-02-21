package org.drachens.fileManagement.filetypes;

import com.google.gson.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GsonStringMaker {
    private final Gson gson;
    @Getter
    private JsonObject config;

    protected GsonStringMaker(String json) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadFromJson(json);
    }

    private void loadFromJson(String json) {
        if (null == json || json.isBlank()){
            this.config = new JsonObject();
            return;
        }
        try {
            this.config = JsonParser.parseString(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            System.err.println("Unable to parse JSON for : " + e.getMessage());
            this.config = new JsonObject();
        }
    }

    public String saveToJson() {
        return gson.toJson(config);
    }


    private JsonObject getOrCreateParent(JsonObject root, String... path) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            if (!current.has(path[i]) || !current.get(path[i]).isJsonObject()) {
                current.add(path[i], new JsonObject());
            }
            current = current.getAsJsonObject(path[i]);
        }
        return current;
    }

    public void addDefault(Long value, String... path) {
        addDefault(new JsonPrimitive(value),path);
    }

    public void addDefault(String value, String... path) {
        addDefault(new JsonPrimitive(value),path);
    }

    public void addDefault(JsonElement value, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        if (!parent.has(key)) {
            parent.add(key, value);
        }
    }

    public void set(Long value, String... path) {
        set(new JsonPrimitive(value),path);
    }

    public void set(String value, String... path) {
        set(new JsonPrimitive(value),path);
    }

    public void set(JsonElement value, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        parent.add(path[path.length - 1], value);
    }

    public <T> void addToList(T value, Class<T> clazz, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        JsonArray array = parent.has(key) ? parent.getAsJsonArray(key) : new JsonArray();
        JsonElement jsonValue = gson.toJsonTree(value, clazz);

        if (!array.contains(jsonValue)) {
            array.add(jsonValue);
            parent.add(key, array);
        }
    }

    public <T> void removeFromList(T value, Class<T> clazz, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        if (!parent.has(key)) return;

        JsonArray array = parent.getAsJsonArray(key);
        JsonElement jsonValue = gson.toJsonTree(value, clazz);

        array.remove(jsonValue);
        parent.add(key, array);
    }

    public <T> List<T> getFromList(Class<T> clazz, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        List<T> list = new ArrayList<>();

        if (!parent.has(key)) return list;

        JsonElement element = parent.get(key);

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (JsonElement jsonElement : array) {
                list.add(gson.fromJson(jsonElement, clazz));
            }
        } else if (element.isJsonPrimitive()) {
            list.add(gson.fromJson(element, clazz));
        } else {
            System.err.println("Error: " + key);
        }

        return list;
    }

    public <T> HashMap<String, T> loadHashMap(Class<T> clazz, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        HashMap<String, T> map = new HashMap<>();

        if (!parent.has(key) || !parent.get(key).isJsonObject()) {
            return map;
        }

        JsonObject jsonObject = parent.getAsJsonObject(key);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), gson.fromJson(entry.getValue(), clazz));
        }

        return map;
    }

    public <T> void saveHashMap(HashMap<String, T> map, Class<T> clazz, String... path) {
        JsonObject parent = getOrCreateParent(config, path);
        String key = path[path.length - 1];

        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue(), clazz));
        }

        parent.add(key, jsonObject);
    }

    protected abstract void initialLoad();

    protected abstract void setDefaults();
}
