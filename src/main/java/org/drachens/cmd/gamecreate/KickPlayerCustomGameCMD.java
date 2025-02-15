package org.drachens.cmd.gamecreate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.customgame.CustomGameKickEvent;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class KickPlayerCustomGameCMD extends Command {
    public KickPlayerCustomGameCMD() {
        super("kick");
        var players = ArgumentType.String("username")
                .setSuggestionCallback((sender,context,suggestion)->{
                    CPlayer p = (CPlayer) sender;
                    List<String> playerss = new ArrayList<>();
                    p.getInstance().getPlayers().forEach(player -> playerss.add(player.getUsername()));
                    getSuggestionBasedOnInput(suggestion,playerss);
                });

        addSyntax((sender,context)->{
            CPlayer p = (CPlayer) sender;
            Player target = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(players));
            if (target==null){
                p.sendMessage(Component.text("That player is null", NamedTextColor.RED));
                return;
            }
            if (!p.getInstance().getPlayers().contains(target)){
                p.sendMessage(Component.text("That player is not in your instance", NamedTextColor.RED));
                return;
            }
            EventDispatcher.call(new CustomGameKickEvent(p, (CPlayer) target));
        },players);
    }
}
