package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import static org.drachens.util.KyoriUtil.compBuild;

public class ElectionTypes {
    private Component name;
    private String identifier;
    public ElectionTypes(TextColor colour, String name){
        this.name = compBuild(name,colour);
        this.identifier = name;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public String getIdentifier(){
        return identifier;
    }
}