package org.drachens.cmd.Dev.Kill;

import net.minestom.server.command.builder.Command;

public class killCMD extends Command {
    public killCMD() {
        super("kill");
        setCondition((sender, s) -> sender.hasPermission("kill"));
        setDefaultExecutor((sender, context) -> {
            if (!sender.hasPermission("kill")) return;
            sender.sendMessage("Proper usage: /kill <all|player|!player>");
        });
        addSubcommand(new player());
        addSubcommand(new notplayer());
        addSubcommand(new all());
    }
}
