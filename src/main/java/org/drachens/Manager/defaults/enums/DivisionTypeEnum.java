package org.drachens.Manager.defaults.enums;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Economics.currency.Payment;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public enum DivisionTypeEnum {
    ww2_cavalry(new DivisionType("Cavalry",2f,1f,3f,2f,1f,new Payment(CurrencyEnum.production,3f),itemBuilder(Material.ORANGE_BANNER))),
    ww2_infantry(new DivisionType("Infantry",2f,2f,5f,1f,1f,new Payment(CurrencyEnum.production,4f),itemBuilder(Material.ORANGE_BANNER))),
    ww2_motorized(new DivisionType("Motorized",3f,2f,4f,3f,1f,new Payment(CurrencyEnum.production,6f),itemBuilder(Material.ORANGE_BANNER))),
    ww2_motorized_artillery(new DivisionType("Motorized Artillery",4f,4f,-4f,3f,1f,new Payment(CurrencyEnum.production,6f),itemBuilder(Material.ORANGE_BANNER))),
    ww2_artillery(new DivisionType("Artillery",3f,3f,-2f,1f,1f,new Payment(CurrencyEnum.production,6f),itemBuilder(Material.ORANGE_BANNER))),
    ww2_tank(new DivisionType("Tank",8f,5f,-5f,3f,1f,new Payment(CurrencyEnum.production,10f),itemBuilder(Material.ORANGE_BANNER)));

    private final DivisionType divisionType;
    DivisionTypeEnum(DivisionType divisionType){
        this.divisionType=divisionType;
    }
    public DivisionType getDivisionType(){
        return divisionType;
    }
}
