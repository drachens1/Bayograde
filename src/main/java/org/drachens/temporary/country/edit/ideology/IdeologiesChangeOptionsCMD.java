package org.drachens.temporary.country.edit.ideology;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class IdeologiesChangeOptionsCMD extends Command {
    public IdeologiesChangeOptionsCMD() {
        super("change-options");

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Ideology ideology = country.getIdeology();
            List<Component> comps = new ArrayList<>();
            ideology.getIdeologies().forEach(((ideologyTypes, aFloat) -> comps.add(Component.text()
                    .append(ideologyTypes.getName())
                    .append(Component.text(" "))
                    .append(Component.text(Math.round(aFloat)))
                    .append(Component.text("%"))
                    .append(Component.text()
                            .append(Component.text(" [BOOST]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to boost the ideology", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country edit ideologies boost " + ideologyTypes.getIdentifier())))
                    .appendNewline()
                    .build())));
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(country.getNameComponent())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(comps)
                    .build());
        });
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
