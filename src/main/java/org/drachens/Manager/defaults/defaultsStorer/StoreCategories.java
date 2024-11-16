package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.store.StoreCategory;

import java.util.HashMap;

public class StoreCategories {
    HashMap<String, org.drachens.store.StoreCategory> storeCategoryHashMap = new HashMap<>();

    public void register(StoreCategory storeCategory) {
        storeCategoryHashMap.put(storeCategory.getIdentifier(), storeCategory);
    }

    public void unregister(String name) {
        storeCategoryHashMap.remove(name);
    }

    public StoreCategory getStoreCategory(String name) {
        return storeCategoryHashMap.get(name);
    }

    public HashMap<String, StoreCategory> getStoreCategories() {
        return storeCategoryHashMap;
    }
}
