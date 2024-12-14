package org.drachens.cmd;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.AdvancementEvent;

public class SpawnCMD extends Command {
    public SpawnCMD() {
        super("spawn");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            p.teleport(new Pos(0, 1, 0));
            EventDispatcher.call(new AdvancementEvent((CPlayer) p,"factoryBuilt"));
        });
    }
}
