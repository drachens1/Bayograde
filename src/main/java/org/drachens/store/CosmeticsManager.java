package org.drachens.store;

import dev.ng5m.CPlayer;

import java.util.HashMap;

public class CosmeticsManager {
    private final HashMap<String, StoreCategory> storeCategoryHashMap = new HashMap<>();
    private final HashMap<String, StoreItem> storeItemHashMap = new HashMap<>();

    public void register(StoreCategory storeCategory) {
        storeCategoryHashMap.put(storeCategory.getIdentifier(), storeCategory);
        storeCategory.getStoreItems().forEach(storeItem -> storeItemHashMap.put(storeItem.getIdentifier(), storeItem));
    }

    public HashMap<String, StoreCategory> getStoreCategories() {
        return storeCategoryHashMap;
    }

    public void addCosmetic(CPlayer p, String identifier) {
        p.getPlayerInfoEntry().addCosmetic(identifier);
        p.addCosmetic(identifier);
    }

    public void removeCosmetic(CPlayer p, String identifier) {
        p.getPlayerInfoEntry().removeCosmetic(identifier);
        p.removeCosmetic(identifier);
    }

    public StoreItem getStoreItem(String identifier) {
        return storeItemHashMap.get(identifier);
    }
}
