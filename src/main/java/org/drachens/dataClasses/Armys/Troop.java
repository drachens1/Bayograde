package org.drachens.dataClasses.Armys;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Troop {
    private ItemDisplay troop;
    private ItemDisplay ally;
    private ItemDisplay enemey;
    private Province province;

    public Troop(Province province, Material item, int modelData){
        this.troop = new ItemDisplay(itemBuilder(item,modelData),province.getPos(),province.getInstance());
        this.province = province;
    }

    public Troop(Province province, Material item, int troop,int ally, int enemy){
        this.troop = new ItemDisplay(itemBuilder(item,troop),province.getPos(),province.getInstance());
        this.ally = new ItemDisplay(itemBuilder(item,ally),province.getPos(),province.getInstance());
        this.enemey = new ItemDisplay(itemBuilder(item,enemy),province.getPos(),province.getInstance());
        this.province = province;
    }

    public Province getProvince(){
        return province;
    }

    public ItemDisplay getAlly(){
        return ally;
    }
    public ItemDisplay getEnemey(){
        return enemey;
    }
    public ItemDisplay getTroop(){
        return troop;
    }
}
