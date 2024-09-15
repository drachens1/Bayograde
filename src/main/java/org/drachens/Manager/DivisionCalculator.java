package org.drachens.Manager;

import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Cost;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CombatUtil.divisionDesignAllVals;
import static org.drachens.util.EconomyUtil.collapseCosts;

public class DivisionCalculator {
    public static void calculateDivStats(DivisionDesign design, Country country, DivisionStats divisionStats) {
        float atk = 0;
        float def = 0;
        float org = 0;
        float speed = 10;
        //add armour + piercing
        List<Cost> costs = new ArrayList<>();
        for (String trop : design.getDesign()) {
            DivisionType d = divisionStats.getDevision(trop);
            costs.add(d.getCost());
            atk += d.getAtk();
            def += d.getDef();
            org += d.getOrg();
            if (d.getSpeed() < speed) {
                speed = d.getSpeed();
            }
        }
        divisionDesignAllVals(design, atk, def, org, speed, collapseCosts(costs));
    }
}
