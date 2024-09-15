package org.drachens.cmd.Dev.help;

import net.minestom.server.command.builder.Command;

import static org.drachens.util.Messages.globalBroadcast;

public class OperatorHelpCMD extends Command {
    public OperatorHelpCMD() {
        super("operator");
        setDefaultExecutor((sender, context) -> {
            globalBroadcast("hi");
        });
    }
}
