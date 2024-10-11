package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.System.ResetEvent;

public class ResetCMD extends Command {
    public ResetCMD() {
        super("reset");
        setCondition((sender,s)->sender.hasPermission("reset"));
        setDefaultExecutor((sender,context)->{
            if (!sender.hasPermission("reset"))return;
            if (!(sender instanceof Player p))return;
            EventDispatcher.call(new ResetEvent(p.getInstance()));
        });
    }
}
