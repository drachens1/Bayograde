package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Countries.IdeologyTypes;

import java.util.HashMap;

public class Ideologies {
    HashMap<String, IdeologyTypes> ideologies = new HashMap<>();

    public void register(IdeologyTypes ideologyTypes) {
        ideologies.put(ideologyTypes.getIdentifier(), ideologyTypes);
    }

    public void unregister(IdeologyTypes ideologyTypes) {
        ideologies.remove(ideologyTypes.getIdentifier());
    }

    public IdeologyTypes getIdeologyType(String name) {
        return ideologies.get(name);
    }

    public HashMap<String, IdeologyTypes> getIdeologies() {
        return ideologies;
    }
}
