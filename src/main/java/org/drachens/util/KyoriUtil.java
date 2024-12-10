package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.intellij.lang.annotations.RegExp;

public class KyoriUtil {
    private static Component factionPrefix;
    private static Component countryPrefix;
    private static Component votePrefix;
    private static Component systemPrefix;
    private static Component allyPrefix;
    private static Component coopPrefix;
    private static Component researchPrefix;
    private static Component nationalAgendasPrefix;

    public static Component replaceString(Component component, @RegExp String from, String to) {
        return component.replaceText(builder -> builder
                .match(from)
                .replacement(to)
        );
    }

    public static Component getPrefixes(String wanted) {
        return switch (wanted) {
            case "faction" -> factionPrefix;
            case "country" -> countryPrefix;
            case "vote" -> votePrefix;
            case "system" -> systemPrefix;
            case "ally" -> allyPrefix;
            case "coop" -> coopPrefix;
            case "research" -> researchPrefix;
            case "agenda" -> nationalAgendasPrefix;
            default -> Component.text().append(Component.text("not found")).build();
        };
    }

    public static void setupPrefixes() {
        factionPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
                .append(Component.text("FACTION", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
                .build();
        countryPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .append(Component.text("COUNTRY", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .build();
        votePrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(Component.text("VOTE", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.GREEN, TextDecoration.BOLD))
                .build();
        systemPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD))
                .append(Component.text("SYSTEM", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD))
                .build();
        allyPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
                .append(Component.text("ALLY", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.BLUE, TextDecoration.BOLD))
                .build();
        coopPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .append(Component.text("CO-OP", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .build();
        researchPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .append(Component.text("RESEARCH", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .build();
        nationalAgendasPrefix = Component.text()
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .append(Component.text("AGENDA", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                .build();
    }}
