package org.drachens.cmd.Dev.gamemode;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;

public class SurvivalCMD extends Command {
    public SurvivalCMD() {
        super("survival");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("gamemode");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("gamemode")) {
                p.setGameMode(GameMode.SURVIVAL);
            }
        });
    }
}
