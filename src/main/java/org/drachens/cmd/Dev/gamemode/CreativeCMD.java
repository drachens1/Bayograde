package org.drachens.cmd.Dev.gamemode;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;

public class CreativeCMD extends Command {
    public CreativeCMD() {
        super("creative");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("gamemode");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("gamemode")) {
                p.setGameMode(GameMode.CREATIVE);
            }
        });
    }
}
