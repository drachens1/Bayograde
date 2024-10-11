package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.bossbars.YearBar;
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;

import java.util.HashMap;
import java.util.List;

public class YearManager {
    HashMap<Instance, YearBar> yearBars = new HashMap<>();
    public YearManager(List<Instance> instances){
        instances.forEach((instance -> yearBars.put(instance, new YearBar(instance))));
        MinecraftServer.getGlobalEventHandler().addListener(StartGameEvent.class,e-> {
            Instance i = e.getInstance();
            if (!yearBars.containsKey(i)) {
                yearBars.put(i,new YearBar(i));
            }
            YearBar yearBar = yearBars.get(i);
            yearBar.run(e.getVotingOption());
        });
        MinecraftServer.getGlobalEventHandler().addListener(ResetEvent.class, e->{
            YearBar yearBar = yearBars.get(e.getInstance());
            yearBar.cancelTask();
        });
    }
    public YearBar getYearBar(Instance instance){
        return yearBars.get(instance);
    }
    public void addBar(Instance instance){
        if (yearBars.containsKey(instance)){
            System.out.println("Already contains this instance");
        }else {
            yearBars.put(instance,new YearBar(instance));
        }
    }
}
