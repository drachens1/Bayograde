package org.drachens.store;

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

    public StoreItem getStoreItem(String identifier) {
        return storeItemHashMap.get(identifier);
    }
}
