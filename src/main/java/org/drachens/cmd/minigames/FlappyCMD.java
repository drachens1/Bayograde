package org.drachens.cmd.minigames;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.miniGameSystem.minigames.FlappyBird;

public class FlappyCMD extends Command {

    public FlappyCMD() {
        super("flappy");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }

            new FlappyBird(p, 40, 32);
        });
    }
}
