package org.drachens.cmd.Dev.gamemode;

import net.minestom.server.command.builder.Command;

public class GamemodeCMD extends Command {
    public GamemodeCMD() {
        super("gamemode");
        setCondition((sender, s) -> sender.hasPermission("gamemode"));
        setDefaultExecutor((sender, context) -> {
            if (sender.hasPermission("gamemode")) sender.sendMessage("Proper usage /gamemode <choice>");
        });
        addSubcommand(new CreativeCMD());
        addSubcommand(new SurvivalCMD());
    }
}
