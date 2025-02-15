package org.drachens.generalGame.country.edit.laws;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class LawsChangeOptionsCMD extends Command {
    public LawsChangeOptionsCMD() {
        super("change-options");

        var lawSet = ArgumentType.String("law")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!isLeaderOfCountry(sender)) return;
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getLawNames());
                });

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (country == null) {
                p.sendMessage(Component.text("Join a country in order to execute this command", NamedTextColor.RED));
                return;
            }
            if (!country.isPlayerLeader(p)) {
                p.sendMessage(Component.text("You are not the leader of a country", NamedTextColor.RED));
                return;
            }
            p.sendMessage(Component.text("Proper usage: /country laws change-options <laws>", NamedTextColor.RED));
        });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            LawCategory lawCategory = country.getLaw(context.get(lawSet));
            if (lawCategory == null) {
                p.sendMessage(Component.text("That law category is null", NamedTextColor.RED));
                return;
            }
            List<Component> comps = new ArrayList<>();
            lawCategory.getLawsStuff().forEach((law -> comps.add(Component.text()
                    .append(law.modifier().getName())
                    .appendNewline()
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the options to change this to", NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.runCommand("/country edit laws change " + lawCategory.getIdentifier() + " " + law.identifier()))
                    .build())));
            comps.removeLast();
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(country.getNameComponent())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Law: " + lawCategory.getIdentifier()))
                    .appendNewline()
                    .append(Component.text("Click to choose the new law", NamedTextColor.GRAY, TextDecoration.ITALIC))
                    .appendNewline()
                    .append(comps));
        }, lawSet);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
