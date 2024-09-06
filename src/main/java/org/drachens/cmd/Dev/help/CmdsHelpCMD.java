package org.drachens.cmd.Dev.help;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

import java.util.Arrays;

public class CmdsHelpCMD extends Command {
    public CmdsHelpCMD() {
        super("cmds");

        setDefaultExecutor(((sender, context) -> {
            StringBuilder commandList = new StringBuilder("Available commands:\n");
            for (Command command : MinecraftServer.getCommandManager().getCommands()) {
                commandList.append("  - ").append(command.getName());
                if (command.getAliases().length > 0) {
                    commandList.append(" (aliases: ").append(Arrays.toString(command.getAliases())).append(")");
                }
                commandList.append("\n");
            }
            sender.sendMessage(commandList.toString());
        }));
    }
}
