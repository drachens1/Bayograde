package org.drachens.cmd.minigames;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.miniGameSystem.minigames.FlappyBird;
import org.drachens.miniGameSystem.minigames.Pacman;
import org.jetbrains.annotations.NotNull;

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
