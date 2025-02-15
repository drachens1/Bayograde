package org.drachens.interfaces;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface Saveable {
    Gson GSON = new Gson();
    default JsonElement toJson() {
        return GSON.toJsonTree(this);
    }
    static <T extends Saveable> T fromJson(JsonElement jsonElement, Class<T> clazz) {
        return GSON.fromJson(jsonElement, clazz);
    }
}
