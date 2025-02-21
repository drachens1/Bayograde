package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum ConditionEnum {
    cant_join_faction(Component.text("You cannot join a faction", NamedTextColor.GRAY)),
    cant_start_a_war(Component.text("You cannot start a war", NamedTextColor.GRAY)),
    cant_puppet(Component.text("You cannot puppet a country", NamedTextColor.GRAY));
    private final Component description;

    ConditionEnum(Component description) {
        this.description = description;
    }

}
