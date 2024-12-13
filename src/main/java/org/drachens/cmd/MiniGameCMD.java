package org.drachens.cmd;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.minigames.Example;

public class MiniGameCMD extends Command {
    public MiniGameCMD() {
        super("minigame");
        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            MiniGame miniGame = new Example(p,50,50);
        }));
    }
}
