package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentListBuilder {
    private final List<Component> components = new ArrayList<>();
    public ComponentListBuilder(){}
    public ComponentListBuilder addComponent(Component component){
        components.add(component);
        return this;
    }
    public List<Component> build(){
        return components;
    }
}
