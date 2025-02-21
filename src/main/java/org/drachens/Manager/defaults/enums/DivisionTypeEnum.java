package org.drachens.Manager.defaults.enums;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Economics.currency.Payment;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public enum DivisionTypeEnum {
    ww2_cavalry(new DivisionType("Cavalry", 2.0f, 1.0f, 3.0f, 2.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 3.0f), itemBuilder(Material.PURPLE_DYE, 0))),
    ww2_infantry(new DivisionType("Infantry", 2.0f, 2.0f, 5.0f, 1.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 4.0f), itemBuilder(Material.PURPLE_DYE, 1))),
    ww2_motorized(new DivisionType("Motorized", 3.0f, 2.0f, 4.0f, 3.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 6.0f), itemBuilder(Material.PURPLE_DYE, 3))),
    ww2_motorized_artillery(new DivisionType("Motorized Artillery", 4.0f, 4.0f, 1.0f, -4.0f, 3.0f, 1.0f, new Payment(CurrencyEnum.production, 6.0f), itemBuilder(Material.PURPLE_DYE, 4))),
    ww2_artillery(new DivisionType("Artillery", 3.0f, 3.0f, -2.0f, 1.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 6.0f), itemBuilder(Material.PURPLE_DYE, 2))),
    ww2_tank(new DivisionType("Tank", 8.0f, 5.0f, -5.0f, 3.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 10.0f), itemBuilder(Material.PURPLE_DYE, 5)));

    private final DivisionType divisionType;

    DivisionTypeEnum(DivisionType divisionType) {
        this.divisionType = divisionType;
    }

    public DivisionType getDivisionType() {
        return divisionType;
    }
}
