package org.drachens.cmd.Dev.gamemode;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;
import org.drachens.player_types.CPlayer;

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
