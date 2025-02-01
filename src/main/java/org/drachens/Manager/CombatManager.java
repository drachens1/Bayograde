package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Province;
import org.drachens.events.NewDay;
import org.drachens.temporary.troops.Combat;

import java.util.HashMap;

public class CombatManager {
    private final HashMap<Instance, HashMap<Province, Combat>> combatHash = new HashMap<>();

    public CombatManager() {
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> {
            HashMap<Province, Combat> a = combatHash.get(e.world());
            if (a == null) return;
            a.forEach((province, combat) -> {
                combat.newDay();
            });
        });
    }

    public void addCombat(Combat combat) {
        HashMap<Province, Combat> e = combatHash.getOrDefault(combat.getProvince().getInstance(), new HashMap<>());
        e.put(combat.getProvince(), combat);
        combatHash.put(combat.getProvince().getInstance(), e);
    }

    public void removeCombat(Combat combat) {
        HashMap<Province, Combat> e = combatHash.getOrDefault(combat.getProvince().getInstance(), new HashMap<>());
        e.remove(combat.getProvince(), combat);
        combatHash.put(combat.getProvince().getInstance(), e);
    }
}
