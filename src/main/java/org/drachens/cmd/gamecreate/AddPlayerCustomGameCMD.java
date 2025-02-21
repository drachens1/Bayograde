package org.drachens.cmd.gamecreate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.customgame.CustomGameInviteEvent;
import org.drachens.player_types.CPlayer;

public class AddPlayerCustomGameCMD extends Command {
    public AddPlayerCustomGameCMD() {
        super("invite");

        ArgumentString players = ArgumentType.String("username");

        addSyntax((sender, context)->{
            CPlayer p = (CPlayer) sender;
            Player target = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(players));
            if (null == target){
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
