package org.drachens.cmd.Dev.help;

import net.minestom.server.command.builder.Command;

public class HelpCMD extends Command {
    public HelpCMD() {
        super("help");

        //Subcommand
        addSubcommand(new OperatorHelpCMD());
        addSubcommand(new CmdsHelpCMD());
        addSubcommand(new HelpPluginsCMD());
    }
}