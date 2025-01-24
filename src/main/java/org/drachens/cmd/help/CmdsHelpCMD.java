package org.drachens.cmd.help;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

import java.util.Arrays;

public class CmdsHelpCMD extends Command {
    public CmdsHelpCMD() {
        super("commands");

        setDefaultExecutor(((sender, context) -> {

        }));
    }
}
