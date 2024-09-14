package org.drachens.cmd;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.ServerUtil.getAllowedChunks;

public class TeleportCMD extends Command {
    public TeleportCMD() {
        super("teleport","tp");
        setDefaultExecutor((sender,context)-> sender.sendMessage("Proper usage: /teleport <x> <y> <z>"));
        var x = ArgumentType.Integer("x");
        var z = ArgumentType.Integer("z");
        addSyntax((sender,context)->{
            if (!(sender instanceof Player p)) {
                return;
            }
            Pos ps = new Pos(context.get(x),1,context.get(z));
            if (!getAllowedChunks().contains(p.getInstance().getChunk(ps.chunkX(),ps.chunkZ()))){
                p.sendMessage(mergeComp(getPrefixes("system"),compBuild("you cannot teleport out of bounds", NamedTextColor.RED)));
                return;
            }
            p.teleport(ps);
        },x,z);
    }
}
