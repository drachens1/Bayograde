package org.drachens.cmd.help;

import net.minestom.server.command.builder.Command;

public class HelpPluginsCMD extends Command {
    public HelpPluginsCMD() {
        super("plugins");
        setDefaultExecutor((sender,context)->{
            //MinecraftServer.
        });
    }
}
