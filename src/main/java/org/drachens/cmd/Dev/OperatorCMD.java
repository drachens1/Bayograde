package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class OperatorCMD extends Command {
    public OperatorCMD() {
        super("operator", "op");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage /op <player>"));
        ArgumentEntity player = ArgumentType.Entity("player");
        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) context.get(player).findFirstPlayer(sender);
            if (null == p) {
                sender.sendMessage("P is null");
                return;
            }
            sender.sendMessage("You have opped " + p.getUsername());
            p.sendMessage("You have been opped");
            ContinentalManagers.permissions.playerOp(p);
        }, player);
    }
}
