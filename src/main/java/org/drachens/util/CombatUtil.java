package org.drachens.util;

import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Economics.currency.Cost;

import java.util.List;

public class CombatUtil {
    public static void divisionDesignAllVals(DivisionDesign design, float atk, float def, float org, float speed, List<Cost> cost){
        design.setAtk(atk);
        design.setDef(def);
        design.setOrg(org);
        design.setSpeed(speed);
        design.setCost(cost);
    }
}
