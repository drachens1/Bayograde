package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;

public class IdeologyTypes {
    private Component name;
    private Component prefix;
    public IdeologyTypes(Component name, Component prefix){
        this.name = name;
        this.prefix = prefix;
    }

    public Component getName() {
        return name;
    }

    public Component getPrefix() {
        return prefix;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }
}
