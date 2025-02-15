package org.drachens.fileManagement.customTypes.player;

import com.google.gson.JsonElement;
import org.drachens.fileManagement.filetypes.GsonStringMaker;

import java.util.HashMap;

public class SaveFiles extends GsonStringMaker {
    private final HashMap<String, JsonElement> saves = new HashMap<>();

    public SaveFiles(String json) {
        super(json);
        initialLoad();
    }

    @Override
    protected void initialLoad() {
        getConfig().entrySet().forEach(entry -> {
            JsonElement value = entry.getValue();
            saves.put(entry.getKey(), value);
        });
    }

    @Override
    protected void setDefaults() {
    }

    public JsonElement getSave(String name) {
        return saves.get(name);
    }

    public void overrideSave(String save, JsonElement jsonElement) {
        saves.put(save, jsonElement);
        set(jsonElement, save);
    }
}
