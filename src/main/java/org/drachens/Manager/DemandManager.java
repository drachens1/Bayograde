package org.drachens.Manager;

import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Diplomacy.demand.Demand;

import java.util.HashMap;
import java.util.UUID;

public class DemandManager {
    private final HashMap<UUID, Demand> active = new HashMap<>();

    public boolean isPlayerActive(Player p) {
        return active.containsKey(p.getUuid());
    }

    public void addActive(Player player, Demand demand) {
        active.put(player.getUuid(), demand);
    }

    public void removeActive(Player player) {
        active.remove(player.getUuid());
    }
    public Demand getDemand(Player player){
        return active.get(player.getUuid());
    }
}
