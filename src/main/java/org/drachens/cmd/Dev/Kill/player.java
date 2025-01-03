package org.drachens.cmd.Dev.Kill;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;

public class player extends Command {
    public player() {
        super("player");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("kill");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasPermission("kill")) return;
            for (Player e : p.getInstance().getPlayers()) {
                e.damage(new Damage(DamageType.SONIC_BOOM, e, p, e.getPosition(), 100f));
            }
        });
    }
}
