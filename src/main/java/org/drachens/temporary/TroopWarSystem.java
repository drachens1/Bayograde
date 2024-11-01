package org.drachens.temporary;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.TrainedTroop;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Armys.TroopType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.interfaces.War;

import java.util.HashMap;

import static org.drachens.util.ServerUtil.blockVecToPos;

public class TroopWarSystem implements War {
    int[] troopWalk = {1, 2, 3, 4};
    int[][] attack = {{3000, 1}, {200, 2}, {300, 3}};
    int[] moving = {5, 4, 6, 4};
    private final TroopType troopType;
    private final TroopPathing troopPathing = new TroopPathing();

    public TroopWarSystem() {
        troopType = new TroopType(
                7, Material.ORANGE_DYE,
                14, Material.ORANGE_DYE,
                21, Material.ORANGE_DYE,
                new Animation(250L, Material.ORANGE_DYE, moving),
                new DynamicAnimation(Material.ORANGE_DYE, attack),
                new Animation(1000L, null, null));
    }

    @Override
    public void onClick(PlayerBlockInteractEvent e) {

    }

    @Override
    public void onClick(PlayerUseItemEvent e) {

    }

    @Override
    public void onClick(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (country == null) return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (province == null || !(province.getOccupier() == country)) return;
        System.out.println("spawn troop");
        TrainedTroop trainedTroop = new TrainedTroop(troopType, new DivisionDesign("design", new HashMap<>(), null, country), 10f);
        Troop troop = new Troop(province, trainedTroop, troopPathing);
    }
}
