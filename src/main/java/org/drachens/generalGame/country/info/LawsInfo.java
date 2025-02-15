package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class LawsInfo extends Command {
    public LawsInfo(String name) {
        super(name);

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.getCountry() != null;
        });

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (country == null) {
                p.sendMessage(Component.text("Join a country in order to execute this command", NamedTextColor.RED));
                return;
            }
            p.sendMessage(getLawsDescription(country, p));
        });
    }

    private Component getLawsDescription(Country country, CPlayer p) {
        List<Component> comps = new ArrayList<>();
        if (country.isPlayerLeader(p)) {
            country.getLaws().forEach(((string, lawCategory) -> comps.add(Component.text()
                    .append(Component.text(string))
                    .append(Component.text(" : "))
                    .append(lawCategory.getCurrent().modifier().getName())
                    .append(Component.text()
                            .append(Component.text(" [CHANGE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the options to change this to", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country edit laws change-options " + string))
                    )
                    .appendNewline()
                    .build())));
        } else {
            country.getLaws().forEach(((string, lawCategory) -> comps.add(Component.text()
                    .append(Component.text(string))
                    .append(Component.text(" : "))
                    .append(lawCategory.getCurrent().modifier().getName())
                    .appendNewline()
                    .build())));
        }

        return Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(country.getNameComponent())
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(comps)
                .build();
    }
}
