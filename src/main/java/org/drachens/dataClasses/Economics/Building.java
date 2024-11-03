package org.drachens.dataClasses.Economics;

import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Building {
    private final BuildTypes buildTypes;
    private final Province province;
    private Country country;
    private final ItemDisplay itemDisplay;
    private int current = 1;
    public Building(BuildTypes buildTypes, Province province){
        this.buildTypes = buildTypes;
        this.province = province;
        this.country = province.getOccupier();
        this.itemDisplay = new ItemDisplay(itemBuilder(buildTypes.getMaterial(),buildTypes.getLvl(0)),province, ItemDisplay.DisplayType.GROUND,true);
        itemDisplay.setPosWithOffset(province);
        country.addBuilding(this);
        province.addBuilding(this);
    }
    public Country getCountry(){
        return country;
    }
    public void capture(Country capturer){
        buildTypes.capture(capturer,this);
    }
    public void upgrade(int amount, Country country, Player p){
        buildTypes.upgrade(amount,this,country,p);
    }
    public void bomb(float bomb){
        buildTypes.bomb(bomb);
    }
    public ItemDisplay getItemDisplay(){
        return itemDisplay;
    }
    public int getCurrentLvl(){
        return current;
    }
    public BuildTypes getBuildTypes(){
        return buildTypes;
    }
    public Province getProvince(){
        return province;
    }
    public void setCurrent(int current){
        this.current = current;
    }
    public void delete(){
        this.itemDisplay.delete();
        this.country.removeBuilding(this);
        this.province.removeBuilding();
    }
    public void setCountry(Country country){
        this.country = country;
    }
}
