package org.drachens.cmd.Dev.Kill;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;

public class all extends Command {
    public all() {
        super("all");
        setCondition((sender,s)->sender.hasPermission("kill"));
        setDefaultExecutor((sender,context)->{
            if (!sender.hasPermission("kill"))return;
            Player p = (Player) sender;
            for (Entity e : p.getInstance().getEntities()){
                if (e instanceof Player ps)ps.damage(new Damage(DamageType.SONIC_BOOM,e,p,e.getPosition(),100f));
                e.remove();
            }
        });
    }
}
