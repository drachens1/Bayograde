package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum ElectionsEnum {
    democratic(Component.text("Democratic", TextColor.color(0,0,255), TextDecoration.BOLD)),
    monarchy(Component.text("Monarchy", NamedTextColor.WHITE)),
    authoritarian(Component.text("Totalitarian",TextColor.color(0,0,0))),
    totalitarian(Component.text("Republic",TextColor.color(0,102,204))),
    republic(Component.text("Authoritarian",TextColor.color(64,64,64)));

    private final Component name;
    ElectionsEnum(Component name){
        this.name=name;
    }
    public Component getName(){
        return name;
    }
}
