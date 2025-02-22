package org.drachens.util;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@Getter
public enum MessageEnum {
    faction(Component.text()
            .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
            .append(Component.text("FACTION", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
            .build()),
    factionChat(Component.text()
            .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
            .append(Component.text("FACTION-CHAT", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
            .build()),
    country(Component.text()
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .append(Component.text("COUNTRY", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .build()),
    countryChat(Component.text()
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .append(Component.text("COUNTRY-CHAT", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .build()),
    puppetChat(Component.text()
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .append(Component.text("PUPPET-CHAT", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .build()),
    vote(Component.text()
            .append(Component.text(" | ", NamedTextColor.GREEN, TextDecoration.BOLD))
            .append(Component.text("VOTE", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.GREEN, TextDecoration.BOLD))
            .build()),
    system(Component.text()
            .append(Component.text(" | ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD))
            .append(Component.text("SYSTEM", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD))
            .build()),
    research(Component.text()
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .append(Component.text("RESEARCH", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
            .build());

    private final Component component;

    MessageEnum(Component component) {
        this.component = component;
    }

}
