package org.drachens.cmd.Dev.gamemode;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;
import org.drachens.player_types.CPlayer;

public class CreativeCMD extends Command {
    public CreativeCMD() {
        super("creative");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("gamemode");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.setGameMode(GameMode.CREATIVE);
        });
    }
}
