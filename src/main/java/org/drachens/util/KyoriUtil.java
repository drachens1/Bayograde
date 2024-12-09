package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.intellij.lang.annotations.RegExp;

public class KyoriUtil {
    private static Component wargoal;
    private static Component countryMembers;
    private static Component countryWars;
    private static Component factionPrefix;
    private static Component countryPrefix;
    private static Component votePrefix;
    private static Component systemPrefix;
    private static Component allyPrefix;
    private static Component coopPrefix;
    private static Component researchPrefix;
    private static Component nationalAgendasPrefix;
    private static Component countryJoin;
    private static Component countryLeave;
    private static Component outOfBounds;
    private static Component broadcastedCountryJoin;

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

    public static Component getCountryMessages(String wanted) {
        return switch (wanted) {
            case "countryLeave" -> countryLeave;
            case "countryJoin" -> countryJoin;
            case "outOfBounds" -> outOfBounds;
            case "broadcastedCountryJoin" -> broadcastedCountryJoin;
            default -> null;
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
        countryLeave = Component.text()
                .append(Component.text("You have left ", NamedTextColor.BLUE))
                .append(Component.text("%country%", NamedTextColor.GOLD, TextDecoration.BOLD))
                .build();
        countryJoin = Component.text()
                .append(Component.text("You have joined ", NamedTextColor.BLUE))
                .append(Component.text("%country%", NamedTextColor.GOLD, TextDecoration.BOLD))
                .build();
        broadcastedCountryJoin = Component.text()
                .append(Component.text("%player%", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" has joined ", NamedTextColor.BLUE))
                .append(Component.text("%country%", NamedTextColor.GOLD, TextDecoration.BOLD))
                .build();
        wargoal = Component.text()
                .append(Component.text(" \n- "))
                .append(Component.text("%country%", NamedTextColor.GOLD))
                .append(Component.text(" time left: %timeleftwar%", NamedTextColor.GOLD))
                .build();
        countryMembers = Component.text()
                .append(Component.text("___________/", NamedTextColor.BLUE))
                .append(Component.text("Members", NamedTextColor.GOLD))
                .append(Component.text("\\__________\n", NamedTextColor.BLUE))
                .build();
        countryWars = Component.text()
                .append(Component.text("___________/", NamedTextColor.BLUE))
                .append(Component.text("Wars", NamedTextColor.GOLD))
                .append(Component.text("\\__________\n", NamedTextColor.BLUE))
                .build();
        outOfBounds = Component.text()
                .append(Component.text("You have went out of bounds! ", NamedTextColor.RED))
                .append(Component.text()
                        .append(Component.text("Click here", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/tp 0 0"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to teleport to spawn", NamedTextColor.GOLD)))
                )
                .append(Component.text(" To teleport to spawn", NamedTextColor.RED))
                .build();
    }}
