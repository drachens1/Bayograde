package org.drachens.cmd.Dev.gamemode;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;

public class GamemodeCMD extends Command {
    public GamemodeCMD() {
        super("gamemode");
        setCondition((sender, s)  ->{
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("gamemode");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("gamemode")) sender.sendMessage("Proper usage /gamemode <choice>");
        });
        addSubcommand(new CreativeCMD());
        addSubcommand(new SurvivalCMD());
    }
}
