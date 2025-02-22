package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countdown;
import org.drachens.events.NewDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountdownManager {
    final HashMap<Instance, List<Countdown>> countdownHashMap = new HashMap<>();
    public CountdownManager(){
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class,e->{
            List<Countdown> toRemove = new ArrayList<>();
            if (!countdownHashMap.containsKey(e.world()))return;
            new ArrayList<>(countdownHashMap.get(e.world())).forEach(countdown -> {
                if (0 >= countdown.removeOne()){
                    countdown.getRunnable().run();
                    toRemove.add(countdown);
                }
                removeCountDowns(toRemove,e.world());
            });

        });
    }
    public void addCountDown(Countdown countdown, Instance instance){
        List<Countdown> countDowns = countdownHashMap.getOrDefault(instance,new ArrayList<>());
        countDowns.add(countdown);
        countdownHashMap.put(instance,countDowns);
    }
    public void removeCountDowns(List<Countdown> countdown, Instance instance){
        List<Countdown> countDowns = countdownHashMap.getOrDefault(instance,new ArrayList<>());
        if (countDowns.removeAll(countdown)){
            countdownHashMap.put(instance,countDowns);
            return;
        }
        countdownHashMap.remove(instance);
    }
}
