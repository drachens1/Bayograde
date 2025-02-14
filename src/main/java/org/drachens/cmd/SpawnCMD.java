package org.drachens.cmd;

import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;

public class SpawnCMD extends Command {
    public SpawnCMD() {
        super("spawn");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            if (p.getInstance() != ContinentalManagers.worldManager.getDefaultWorld().getInstance()) {
                p.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
            }
            p.teleport(new Pos(0, 1, 0));
        });
    }
}
