package org.drachens.cmd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.player_types.CPlayer;

public class PingCMD extends Command {
    public PingCMD() {
        super("ping");
        Component errorNotFound = Component.text("Player not found",NamedTextColor.RED);
        var player = ArgumentType.Entity("player");
        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.sendMessage(format(p.getLatency()));
        }));
        addSyntax(((sender, context) -> {
            CPlayer p = (CPlayer) context.get(player).findFirstPlayer(sender);
            if (p==null){
                sender.sendMessage(errorNotFound);
                return;
            }
            sender.sendMessage(format(p.getLatency()));
        }),player);
    }

    private Component format(int latency){
        return Component.text(latency+"ms", NamedTextColor.GREEN);
    }
}
