package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countdown;
import org.drachens.events.NewDay;

import java.util.*;

public class CountdownManager {
    private final Map<UUID, List<Countdown>> countdownHashMap = new WeakHashMap<>();

    public CountdownManager() {
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> {
            List<Countdown> countDowns = countdownHashMap.get(e.world().getUuid());
            if (countDowns == null) return;

            Iterator<Countdown> iterator = new ArrayList<>(countDowns).iterator();
            while (iterator.hasNext()) {
                Countdown countdown = iterator.next();
                if (countdown.removeOne() <= 0) {
                    countdown.getRunnable().run();
                    iterator.remove();
                }
            }

            if (countDowns.isEmpty()) {
                countdownHashMap.remove(e.world().getUuid());
            }
        });
    }

    public void addCountDown(Countdown countdown, Instance instance) {
        countdownHashMap.computeIfAbsent(instance.getUuid(), k -> new ArrayList<>()).add(countdown);
    }
}