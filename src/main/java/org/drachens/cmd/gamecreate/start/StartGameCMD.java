package org.drachens.cmd.gamecreate.start;

import net.minestom.server.command.builder.Command;

public class StartGameCMD extends Command {
    public StartGameCMD() {
        super("start-game");

        addSubcommand(new NewStartGameCMD());
        addSubcommand(new LoadStartGameCMD());
    }
}
