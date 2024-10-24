package org.drachens.dataClasses.Armys;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Troop {
    private final ItemDisplay troop;
    private ItemDisplay ally;
    private ItemDisplay enemey;
    private final Province province;
    private final TroopType troopType;
    private final Country country;
    private final AStarPathfinderVoids troopPathing;
    public Troop(Province province, Country country, TroopType troopType, AStarPathfinderVoids troopPathing) {
        this.troopPathing = troopPathing;
        this.troopType = troopType;
        this.country = country;
        Pos pos = province.getPos().add(0.5, 1.5, 0.5);
        this.troop = new ItemDisplay(itemBuilder(troopType.getItem(), troopType.getModelData()), pos, province.getInstance(), ItemDisplay.DisplayType.GROUND, true);
        this.province = province;
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(troop::addViewer);
    }

    public Troop(Province province, Country country, TroopType troopType, int troop, int ally, int enemy, AStarPathfinderVoids troopPathing) {
        this.troopPathing = troopPathing;
        this.troopType = troopType;
        this.country = country;
        Pos pos = province.getPos().add(0.5, 1.5, 0.5);
        this.troop = new ItemDisplay(itemBuilder(troopType.getItem(), troop), pos, province.getInstance(), ItemDisplay.DisplayType.GROUND, true);
        this.ally = new ItemDisplay(itemBuilder(troopType.getItem(), ally), province, ItemDisplay.DisplayType.GROUND, true);
        this.enemey = new ItemDisplay(itemBuilder(troopType.getItem(), enemy), province, ItemDisplay.DisplayType.GROUND, true);
        this.province = province;
    }

    public Province getProvince() {
        return province;
    }

    public ItemDisplay getAlly() {
        return ally;
    }

    public ItemDisplay getEnemey() {
        return enemey;
    }

    public ItemDisplay getTroop() {
        return troop;
    }

    public void move(Province to){
        troopType.getMoveAnimation().start(troop,true);
        country.getaStarPathfinder().findPath(this.province,to,country,troopPathing);
        troopType.getMoveAnimation().stop(troop);
    }
    public void attack(Province to){

    }

    public void testAnimation(){
        this.troopType.getMoveAnimation().start(troop,true);
    }
}
