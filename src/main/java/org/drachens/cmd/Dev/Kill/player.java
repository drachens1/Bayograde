package org.drachens.cmd.Dev.Kill;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;

public class player extends Command {
    public player() {
        super("player");
        setCondition((sender,s)->sender.hasPermission("kill"));
        setDefaultExecutor((sender,context)->{
            if (!sender.hasPermission("kill"))return;
            Player p = (Player) sender;
            for (Player e : p.getInstance().getPlayers()){
                e.damage(new Damage(DamageType.SONIC_BOOM,e,p,e.getPosition(),100f));
            }
        });
    }
}
