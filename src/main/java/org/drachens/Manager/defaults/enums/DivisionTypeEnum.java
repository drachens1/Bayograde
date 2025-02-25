package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Economics.currency.Payment;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
public enum DivisionTypeEnum {
    ww2_cavalry(new DivisionType("Cavalry", 3.0f, 1.5f, 4.0f, 10.0f, 3.0f, 1.0f, new Payment(CurrencyEnum.production, 3.0f), itemBuilder(Material.PURPLE_DYE, 0))),
    ww2_infantry(new DivisionType("Infantry", 2.5f, 2.5f, 5.0f, 15.0f, 1.5f, 1.0f, new Payment(CurrencyEnum.production, 4.0f), itemBuilder(Material.PURPLE_DYE, 1))),
    ww2_motorized(new DivisionType("Motorized", 3.5f, 2.5f, 4.5f, 18.0f, 4.0f, 1.0f, new Payment(CurrencyEnum.production, 6.0f), itemBuilder(Material.PURPLE_DYE, 3))),
    ww2_motorized_artillery(new DivisionType("Motorized Artillery", 5.0f, 3.0f, 1.0f, 12.0f, 3.5f, 1.0f, new Payment(CurrencyEnum.production, 7.0f), itemBuilder(Material.PURPLE_DYE, 4))),
    ww2_artillery(new DivisionType("Artillery", 4.0f, 2.0f, 0.5f, 10.0f, 1.0f, 1.0f, new Payment(CurrencyEnum.production, 5.0f), itemBuilder(Material.PURPLE_DYE, 2))),
    ww2_tank(new DivisionType("Tank", 7.0f, 5.0f, 2.0f, 30.0f, 3.0f, 1.5f, new Payment(CurrencyEnum.production, 12.0f), itemBuilder(Material.PURPLE_DYE, 5)));

    private final DivisionType divisionType;

    DivisionTypeEnum(DivisionType divisionType) {
        this.divisionType = divisionType;
    }

}
