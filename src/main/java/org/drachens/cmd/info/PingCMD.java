package org.drachens.cmd.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class PingCMD extends Command {
    public PingCMD() {
        super("ping");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            p.sendMessage(Component.text()
                    .append(Component.text(p.getLatency(), NamedTextColor.GREEN))
                    .append(Component.text(" MS", NamedTextColor.GREEN)));
        });
    }
}
