package org.drachens.Manager.defaults.enums;

import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Economics.currency.Payment;

public enum DivisionTypeEnum {
    ww2_cavalry(new DivisionType("Cavalry",2f,1f,3f,2f,new Payment(CurrencyEnum.production,3f))),
    ww2_infantry(new DivisionType("Infantry",2f,2f,5f,1f,new Payment(CurrencyEnum.production,4f))),
    ww2_motorized(new DivisionType("Motorized",3f,2f,4f,3f,new Payment(CurrencyEnum.production,6f))),
    ww2_motorized_artillery(new DivisionType("Motorized Artillery",4f,4f,-4f,3f,new Payment(CurrencyEnum.production,6f))),
    ww2_artillery(new DivisionType("Artillery",3f,3f,-2f,1f,new Payment(CurrencyEnum.production,6f))),
    ww2_tank(new DivisionType("Tank",8f,5f,-5f,3f,new Payment(CurrencyEnum.production,10f)));

    private final DivisionType divisionType;
    DivisionTypeEnum(DivisionType divisionType){
        this.divisionType=divisionType;
    }
    public DivisionType getDivisionType(){
        return divisionType;
    }
}
