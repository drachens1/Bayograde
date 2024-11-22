package org.drachens.Manager.defaults.scheduler;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContinentalSchedulerManager {

    private final HashMap<Class<? extends Event>, HashMap<Instance, List<ContinentalScheduler>>> eventScheduler = new HashMap<>();
    private final HashMap<Class<? extends Event>, List<ContinentalScheduler>> globalEventScheduler = new HashMap<>();
    private final GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

    public ContinentalSchedulerManager() {
        updateListeners();
    }

    public void unregister(ContinentalScheduler continentalScheduler) {
        boolean global = continentalScheduler.isGlobal();
        Class<? extends Event> event = continentalScheduler.getEvent();
        if (global) {
            globalEventScheduler.computeIfAbsent(event, k -> new ArrayList<>()).remove(continentalScheduler);
        } else {
            Instance instance1 = continentalScheduler.getInstance();
            eventScheduler.computeIfAbsent(event, k -> new HashMap<>())
                    .computeIfAbsent(instance1, k -> new ArrayList<>())
                    .add(continentalScheduler);
        }

    }

    public void register(ContinentalScheduler continentalScheduler) {
        boolean global = continentalScheduler.isGlobal();
        Class<? extends Event> event = continentalScheduler.getEvent();
        if (global) {
            globalEventScheduler.computeIfAbsent(event, k -> {
                globalEventHandler.addListener(event, e -> {
                    for (ContinentalScheduler c : globalEventScheduler.get(event)) {
                        c.AttemptRun(e.getInstance(), e);
                    }
                });
                return new ArrayList<>();
            }).add(continentalScheduler);
        } else {
            Instance instance1 = continentalScheduler.getInstance();
            eventScheduler.computeIfAbsent(continentalScheduler.getEvent(), k -> new HashMap<>())
                    .computeIfAbsent(instance1, k -> {
                        globalEventHandler.addListener(event, e -> {
                            Instance instance = e.getInstance();
                            List<ContinentalScheduler> schedulers = eventScheduler.get(event).get(instance);
                            if (schedulers != null) {
                                for (ContinentalScheduler c : schedulers) {
                                    c.AttemptRun(instance, e);
                                }
                            }
                        });
                        return new ArrayList<>();
                    })
                    .add(continentalScheduler);
        }

    }

    public void updateListeners() {
        eventScheduler.forEach((event, schedulersByInstance) -> globalEventHandler.addListener(event, e -> {
            Instance instance = e.getInstance();
            List<ContinentalScheduler> schedulers = eventScheduler.get(event).get(instance);
            if (schedulers != null) {
                for (ContinentalScheduler c : schedulers) {
                    c.AttemptRun(instance, e);
                }
            }
        }));

        globalEventScheduler.forEach((event, schedulers) -> globalEventHandler.addListener(event, e -> {
            for (ContinentalScheduler c : schedulers) {
                c.AttemptRun(e.getInstance(), e);
            }
        }));
    }
}
