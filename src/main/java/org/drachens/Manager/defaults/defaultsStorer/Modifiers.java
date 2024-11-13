package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Modifier;

import java.util.HashMap;

public class Modifiers {        //Name system = votingName-modifier
    HashMap<String, Modifier> modifiers = new HashMap<>();

    public void register(Modifier modifier, String name) {
        modifiers.put(name, modifier);
    }

    public void unregister(String name) {
        modifiers.remove(name);
    }

    public Modifier getModifier(String name) {
        return modifiers.get(name);
    }

    public HashMap<String, Modifier> getModifiers() {
        return modifiers;
    }
}
