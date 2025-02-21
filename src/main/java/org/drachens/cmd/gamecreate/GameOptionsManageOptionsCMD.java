package org.drachens.cmd.gamecreate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameOptionsManageOptionsCMD extends Command {
    public GameOptionsManageOptionsCMD() {
        super("options");
        setDefaultExecutor((sender, context)->{
            CPlayer p = (CPlayer) sender;
            List<Component> comps = new ArrayList<>();
            if (p.isLeaderOfOwnGame()) {
                comps.add(Component.text()
                        .append(Component.text("Creation: "))
                        .appendNewline()
                        .append(Component.text()
                                .append(Component.text(" [COMPLETE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to complete the waiting period", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/manage creation complete")))
                        .append(Component.text()
                                .append(Component.text(" [CANCEL] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to cancel the creation", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/manage creation cancel")))
                        .appendNewline()
                        .append(Component.text("Players: "))
                        .appendNewline()
                        .append(Component.text()
                                .append(Component.text(" [INVITE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Gives a prompt to invite someone", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.suggestCommand("/manage invite ")))
                        .append(Component.text()
                                .append(Component.text(" [KICK] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Gives a prompt to kick someone", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.suggestCommand("/manage kick ")))

                        .build());
            }

            p.sendMessage(Component.text()
                    .append(comps)
                    .build());
        });
    }
}
