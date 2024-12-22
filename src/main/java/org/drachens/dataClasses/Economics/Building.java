package org.drachens.dataClasses.Economics;

import dev.ng5m.CPlayer;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.dataClasses.territories.Province;

import java.util.HashSet;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Building {
    private final HashSet<String> synonyms;
    private final BuildingEnum buildType;
    private final Province province;
    private final ItemDisplay itemDisplay;
    private Country country;
    private int current = 1;

    public Building(BuildTypes buildTypes, Province province) {
        this.buildType = buildTypes.getIdentifier();
        this.synonyms = buildTypes.getSynonyms();
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
        buildType.getBuildTypes().capture(capturer, this);
    }

    public void upgrade(int amount, Country country, CPlayer p) {
        buildType.getBuildTypes().upgrade(amount, this, country, p);
    }

    public void bomb(float bomb) {
        buildType.getBuildTypes().bomb(bomb);
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

    public HashSet<String> getSynonyms() {
        return synonyms;
    }

    public Province getProvince() {
        return province;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean hasSynonym(BuildingEnum buildingEnum) {
        return synonyms.contains(buildingEnum);
    }

    public void delete() {
        this.itemDisplay.delete();
        this.country.removeBuilding(this);
        this.province.removeBuilding();
    }
}
