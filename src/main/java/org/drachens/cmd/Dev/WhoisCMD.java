package org.drachens.cmd.Dev;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class WhoisCMD extends Command {
    public WhoisCMD() {
        super("whois");

        var player = ArgumentType.String("player")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    List<String> s = new ArrayList<>();
                    MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player1 -> s.add(player1.getUsername()));
                    getSuggestionBasedOnInput(suggestion, s);
                });

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.getUsername().equalsIgnoreCase("drachens") || p.getUsername().equalsIgnoreCase("sweeville");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!(p.getUsername().equalsIgnoreCase("drachens") || p.getUsername().equalsIgnoreCase("sweeville")))
                return;
            CPlayer target = (CPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(player));
            if (target == null) {
                p.sendMessage(Component.text("That is not a valid player", NamedTextColor.RED));
                return;
            }
            List<String> permissions = new ArrayList<>(p.getAllPermissionss());
            p.sendMessage(Component.text()
                    .append(Component.text(p.getUsername()))
                    .appendNewline()
                    .append(Component.text("Playtime: "))
                    .append(Component.text(p.getPlayTimeString()))
                    .appendNewline()
                    .append(Component.text("Permissions: "))
                    .append(Component.text(permissions.toString()))
                    .appendNewline()
                    .build());
        }, player);
    }
}
