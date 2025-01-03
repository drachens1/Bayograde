package org.drachens.cmd.Dev.Kill;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;

public class killCMD extends Command {
    public killCMD() {
        super("kill");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("kill");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasPermission("kill")) return;
            sender.sendMessage("Proper usage: /kill <all|player|!player>");
        });
        addSubcommand(new player());
        addSubcommand(new notplayer());
        addSubcommand(new all());
    }
}
