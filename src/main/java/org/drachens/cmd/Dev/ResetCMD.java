package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.system.ResetEvent;

public class ResetCMD extends Command {
    public ResetCMD() {
        super("reset");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("reset");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasPermission("reset")) return;
            EventDispatcher.call(new ResetEvent(p.getInstance()));
        });
    }
}
