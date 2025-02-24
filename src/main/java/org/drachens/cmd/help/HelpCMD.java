package org.drachens.cmd.help;

import net.minestom.server.command.builder.Command;

public class HelpCMD extends Command {
    public HelpCMD() {
        super("help");

        //Subcommand
        addSubcommand(new HelpCountryCMD());
        addSubcommand(new HelpPluginsCMD());
    }
}