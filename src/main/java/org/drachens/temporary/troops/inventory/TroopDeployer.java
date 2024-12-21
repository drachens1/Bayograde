package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Armys.TroopType;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue.TrainedTroop;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.temporary.troops.TroopPathing;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.ServerUtil.blockVecToPos;

public class TroopDeployer extends HotbarItemButton {
    private final TroopType troopType;
    private final TroopPathing troopPathing = new TroopPathing();
    int[][] attack = {{3000, 1}, {200, 2}, {300, 3}};
    int[] moving = {5, 4, 6, 4};

    public TroopDeployer() {
        super(1, itemBuilder(Material.GOLD_BLOCK));
        troopType = new TroopType(
                7, Material.ORANGE_DYE,
                14, Material.ORANGE_DYE,
                21, Material.ORANGE_DYE,
                new Animation(250L, Material.ORANGE_DYE, moving),
                new DynamicAnimation(Material.ORANGE_DYE, attack),
                new Animation(1000L, null, null));
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        TroopCountry country = (TroopCountry) p.getCountry();
        if (country == null) return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (province == null || !(province.getOccupier() == country)) return;
        System.out.println("spawn troop");
        TrainedTroop trainedTroop = new TrainedTroop(troopType, new DivisionDesign("design", new HashMap<>(), country), 10f);
        new Troop(province, trainedTroop, troopPathing);
    }
}
