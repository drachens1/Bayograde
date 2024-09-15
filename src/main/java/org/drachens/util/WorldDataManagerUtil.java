package org.drachens.util;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.DivisionStats;

import java.util.HashMap;

public class WorldDataManagerUtil {
    private static final HashMap<Instance, DivisionStats> divisionStatsHashMap = new HashMap<>();

    public static void setDivisionStats(Instance instance, DivisionStats divisionStats) {
        divisionStatsHashMap.put(instance, divisionStats);
    }

    public static DivisionStats getDivisionStats(Instance instance) {
        return divisionStatsHashMap.get(instance);
    }

    public static void removeDivisionStats(Instance instance) {
        divisionStatsHashMap.remove(instance);
    }
}
