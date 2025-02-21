package org.drachens.cmd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.drachens.util.MessageEnum;

import static org.drachens.util.ServerUtil.getAllowedChunks;

public class TeleportCMD extends Command {
    public TeleportCMD() {
        super("teleport", "tp");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /teleport <x> <y> <z>"));
        ArgumentInteger x = ArgumentType.Integer("x");
        ArgumentInteger y = ArgumentType.Integer("y");
        ArgumentInteger z = ArgumentType.Integer("z");
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            Pos ps = new Pos(context.get(x), 1, context.get(z));
            if (!getAllowedChunks().contains(p.getInstance().getChunk(ps.chunkX(), ps.chunkZ()))) {
                p.sendMessage(Component.text().append(MessageEnum.system.getComponent(), Component.text("you cannot teleport out of bounds", NamedTextColor.RED)).build());
                return;
            }
            p.teleport(ps);
        }, x, z);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            Pos ps = new Pos(context.get(x), context.get(y), context.get(z));
            if (!getAllowedChunks().contains(p.getInstance().getChunk(ps.chunkX(), ps.chunkZ()))) {
                p.sendMessage(Component.text().append(MessageEnum.system.getComponent(), Component.text("you cannot teleport out of bounds", NamedTextColor.RED)).build());
                return;
            }
            p.teleport(ps);
        }, x, y, z);
    }
}
