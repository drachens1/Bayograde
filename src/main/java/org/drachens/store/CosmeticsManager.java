package org.drachens.store;

import dev.ng5m.CPlayer;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.ConfigFileManager;

import java.util.HashMap;

public class CosmeticsManager {
    private final HashMap<String, StoreCategory> storeCategoryHashMap = new HashMap<>();
    private final HashMap<String, StoreItem> storeItemHashMap = new HashMap<>();
    private final ConfigFileManager configFileManager = ContinentalManagers.configFileManager;

    public void register(StoreCategory storeCategory) {
        storeCategoryHashMap.put(storeCategory.getIdentifier(), storeCategory);
        storeCategory.getStoreItems().forEach(storeItem -> storeItemHashMap.put(storeItem.getIdentifier(),storeItem));
    }

    public StoreCategory getStoreCategory(String name) {
        return storeCategoryHashMap.get(name);
    }

    public HashMap<String, StoreCategory> getStoreCategories() {
        return storeCategoryHashMap;
    }
    public void addCosmetic(CPlayer p, String identifier){
       p.getPlayerDataFile().addCosmetic(identifier);
       p.addCosmetic(identifier);
    }
    public void removeCosmetic(CPlayer p, String identifier){
        p.getPlayerDataFile().removeCosmetic(identifier);
        p.removeCosmetic(identifier);
    }
    public StoreItem getStoreItem(String identifier){
        return storeItemHashMap.get(identifier);
    }
}
