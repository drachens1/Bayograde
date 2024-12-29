package org.drachens.cmd.minigames;

import net.minestom.server.command.builder.Command;
public class MinigamesCMD extends Command {
    public MinigamesCMD() {
        super("minigames");

        addSubcommand(new FlappyCMD());
        addSubcommand(new PacmanCMD());
    }
}
