package org.drachens.cmd.Dev.Kill;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

public class notplayer extends Command {
    public notplayer() {
        super("!player");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("kill");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasPermission("kill")) return;
            for (Entity e : p.getInstance().getEntities()) {
                if (!(e instanceof Player)) e.remove();
            }
        });
    }
}
