package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countdown;
import org.drachens.events.NewDay;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CountdownManager {
    private final Map<UUID, List<Countdown>> countdownHashMap = new WeakHashMap<>();

    public CountdownManager() {
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> {
            UUID worldUUID = e.world().getUuid();
            List<Countdown> countDowns = countdownHashMap.get(worldUUID);
            if (countDowns == null) return;

            List<Countdown> updatedList = new ArrayList<>();
            for (Countdown countdown : new ArrayList<>(countDowns)) {
                if (countdown.removeOne() > 0) {
                    updatedList.add(countdown);
                } else {
                    countdown.getRunnable().run();
                }
            }

            if (updatedList.isEmpty()) {
                countdownHashMap.remove(worldUUID);
            } else {
                countdownHashMap.put(worldUUID, updatedList);
            }
        });
    }

    public void addCountDown(Countdown countdown, Instance instance) {
        countdownHashMap.computeIfAbsent(instance.getUuid(), k -> new CopyOnWriteArrayList<>()).add(countdown);
    }
}

