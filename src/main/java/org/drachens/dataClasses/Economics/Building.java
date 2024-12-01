package org.drachens.dataClasses.Economics;

import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.BuildingTypes;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Building {
    private final BuildingTypes buildingTypes = ContinentalManagers.defaultsStorer.buildingTypes;
    private final BuildingEnum buildType;
    private final Province province;
    private final ItemDisplay itemDisplay;
    private Country country;
    private int current = 1;

    public Building(BuildTypes buildTypes, Province province) {
        this.buildType = buildTypes.getIdentifier();
        this.province = province;
        this.country = province.getOccupier();
        this.itemDisplay = new ItemDisplay(itemBuilder(buildTypes.getMaterial(), buildTypes.getLvl(0)), province, ItemDisplay.DisplayType.GROUND, true);
        itemDisplay.setPosWithOffset(province);
        country.addBuilding(this);
        province.addBuilding(this);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void capture(Country capturer) {
        buildingTypes.getBuildType(buildType).capture(capturer, this);
    }

    public void upgrade(int amount, Country country, Player p) {
        buildingTypes.getBuildType(buildType).upgrade(amount, this, country, p);
    }

    public void bomb(float bomb) {
        buildingTypes.getBuildType(buildType).bomb(bomb);
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public int getCurrentLvl() {
        return current;
    }

    public BuildingEnum getBuildTypes() {
        return buildType;
    }

    public Province getProvince() {
        return province;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void delete() {
        this.itemDisplay.delete();
        this.country.removeBuilding(this);
        this.province.removeBuilding();
    }
}
