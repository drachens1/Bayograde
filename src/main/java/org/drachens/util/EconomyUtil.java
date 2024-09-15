package org.drachens.util;

import org.drachens.dataClasses.Economics.currency.Cost;

import java.util.List;

public class EconomyUtil {
    public static List<Cost> collapseCosts(List<Cost> costs) {
        for (Cost cost : costs) {
            costs.removeIf(cost::add);
        }
        return costs;
    }
}
