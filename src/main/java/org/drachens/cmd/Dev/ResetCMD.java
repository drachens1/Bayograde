package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.system.ResetEvent;
import org.drachens.player_types.CPlayer;

public class ResetCMD extends Command {
    public ResetCMD() {
        super("reset");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("reset");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            EventDispatcher.call(new ResetEvent(p.getInstance()));
        });
    }
}
