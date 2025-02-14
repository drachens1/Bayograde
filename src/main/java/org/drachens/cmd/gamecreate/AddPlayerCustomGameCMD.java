package org.drachens.cmd.gamecreate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.customgame.CustomGameInviteEvent;
import org.drachens.player_types.CPlayer;

public class AddPlayerCustomGameCMD extends Command {
    public AddPlayerCustomGameCMD() {
        super("invite");


        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame();
        });

        var players = ArgumentType.String("username");

        addSyntax((sender,context)->{
            CPlayer p = (CPlayer) sender;
            if (!p.isLeaderOfOwnGame())return;
            Player target = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(players));
            if (target==null){
                p.sendMessage(Component.text("That player is null", NamedTextColor.RED));
                return;
            }
            if (target.getInstance()==p.getInstance()){
                p.sendMessage(Component.text("They are already in your instance",NamedTextColor.RED));
                return;
            }
            EventDispatcher.call(new CustomGameInviteEvent(p, (CPlayer) target));
        },players);
    }
}
