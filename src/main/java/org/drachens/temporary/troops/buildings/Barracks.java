package org.drachens.temporary.troops.buildings;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.territories.Province;

public class Barracks extends BuildTypes {
    public Barracks() {
        super(new int[]{0}, Material.BROWN_DYE, BuildingEnum.barracks);
    }

    @Override
    public void onBuild(Country country, Province province, Player p) {

    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        return false;
    }

    @Override
    public boolean requirementsToUpgrade(Building building, Country country, int add, Player p) {
        return false;
    }

    @Override
    public boolean requirementsToDestroy(Country country) {
        return false;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {

    }

    @Override
    protected void bombed(float dmg) {

    }

    @Override
    protected void onDestroyed(Building building) {

    }

    @Override
    protected void onUpgrade(int amount, Building building) {

    }
}
