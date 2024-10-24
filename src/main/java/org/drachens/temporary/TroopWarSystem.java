package org.drachens.temporary;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Armys.TroopType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.interfaces.War;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;
import static org.drachens.util.ServerUtil.blockVecToPos;

public class TroopWarSystem implements War {
    int[] troopWalk = {1,2,3,4};
    int[][] attack = {{3000,1},{200,2},{300,3}};
    int[][] moving = {{1000,5},{1000,4},{1000,6},{1000,4}};
    private final TroopType troopType;
    private final TroopPathing troopPathing = new TroopPathing();
    public TroopWarSystem(){
        troopType = new TroopType(7, Material.ORANGE_DYE
                ,new DynamicAnimation(Material.ORANGE_DYE,moving),
                new DynamicAnimation(Material.ORANGE_DYE,attack),
                new Animation(1f,null,null));
    }
    @Override
    public void onClick(PlayerBlockInteractEvent e) {

    }

    @Override
    public void onClick(PlayerUseItemEvent e) {

    }

    @Override
    public void onClick(PlayerStartDiggingEvent e) {
        Player p = e.getPlayer();
        Country country = getCountryFromPlayer(p);
        if (country==null)return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (province==null || !(province.getOccupier()==country))return;
        System.out.println("spawn troop");
        Troop troop = new Troop(province,country,troopType,troopPathing);
        troop.testAnimation();
    }
}
