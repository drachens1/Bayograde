package org.drachens.cmd.help;

import net.minestom.server.command.builder.Command;

import static org.drachens.api.util.Messages.globalBroadcast;

public class OperatorHelpCMD extends Command {
    public OperatorHelpCMD() {
        super("operator");
        setDefaultExecutor((sender,context)->{
            globalBroadcast("hi");
        });
    }
}
