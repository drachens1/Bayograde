package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Countries.ElectionTypes;

import java.util.HashMap;

public class Elections {
    HashMap<String, ElectionTypes> electionTypesHashMap = new HashMap<>();

    public void register(ElectionTypes electionTypes) {
        electionTypesHashMap.put(electionTypes.getIdentifier(), electionTypes);
    }

    public void unregister(ElectionTypes electionTypes) {
        electionTypesHashMap.remove(electionTypes.getIdentifier());
    }

    public ElectionTypes getElectionTypes(String name) {
        return electionTypesHashMap.get(name);
    }

    public HashMap<String, ElectionTypes> getElectionTypes() {
        return electionTypesHashMap;
    }
}
