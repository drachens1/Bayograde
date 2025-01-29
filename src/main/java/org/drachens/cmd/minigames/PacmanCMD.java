package org.drachens.cmd.minigames;

import net.minestom.server.command.builder.Command;
import org.drachens.miniGameSystem.minigames.Pacman;
import org.drachens.player_types.CPlayer;

public class PacmanCMD extends Command {
    public PacmanCMD() {
        super("pacman");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            new Pacman(p);
        });
    }
}
