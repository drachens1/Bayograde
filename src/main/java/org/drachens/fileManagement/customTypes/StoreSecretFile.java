package org.drachens.fileManagement.customTypes;

import org.drachens.fileManagement.filetypes.GsonFileType;

import java.util.HashMap;

public class StoreSecretFile extends GsonFileType {
    public StoreSecretFile() {
        super("store.json");
        setDefaults();
    }

    @Override
    protected void initialLoad() {}

    @Override
    protected void setDefaults() {
        addDefault("","store","secret");
        if (!getConfig().getAsJsonObject("store").has("id-map")){
            HashMap<String,String> s = new HashMap<>();
            s.put("one","two");
            saveHashMap(s,String.class,"store","id-map");
        }
        saveToFile();
    }

    public String getSecret(){
        return getConfig().getAsJsonObject("store").get("secret").getAsString();
    }

    public HashMap<String, String> getIdMap(){
        return loadHashMap(String.class,"store","id-map");
    }
}
