package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Province;

public class CheckNeighbours extends Command {
    public CheckNeighbours() {
        super("check-neighbours");
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Province prov = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(p.getTargetBlockPosition(5));
            for (Province neighbour : prov.getNeighbours()) {
                neighbour.setBlock(Material.DIAMOND_BLOCK);
            }
        });
    }
}
