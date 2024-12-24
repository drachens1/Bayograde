package org.drachens.Manager;

import net.minestom.server.instance.Instance;
import org.drachens.bossbars.YearBar;

import java.util.HashMap;

public class YearManager {
    HashMap<Instance, YearBar> yearBars = new HashMap<>();

    public YearBar getYearBar(Instance instance) {
        return yearBars.get(instance);
    }

    public void addBar(Instance instance) {
        if (yearBars.containsKey(instance)) {
            System.out.println("Already contains this instance");
        } else {
            yearBars.put(instance, new YearBar(instance));
        }
    }

    public boolean contains(Instance instance){
        return yearBars.containsKey(instance);
    }
}
