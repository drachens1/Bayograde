package org.drachens.temporary.country.edit;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

public class EditOptionsCMD extends Command {
    public EditOptionsCMD() {
        super("options");

        setCondition((sender,s)->isLeaderOfCountry(sender));

        setDefaultExecutor((sender,context)->{
            if (!isLeaderOfCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(country.getNameComponent())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[LAWS] ",NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the law options", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country edit laws info")))
                    .append(Component.text()
                            .append(Component.text(" [IDEOLOGIES] ",NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the ideologies options", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country edit ideologies change-options")))
                    .append(Component.text()
                            .append(Component.text(" [RESEARCH] ",NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to show the research tech tree", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country research full-tree")))
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
