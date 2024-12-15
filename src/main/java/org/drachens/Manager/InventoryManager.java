package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.interfaces.items.HotbarInventory;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryManager {
    private final HashMap<Player, HotbarInventory> activeHotBar = new HashMap<>();
    private final List<Player> playersCooldown = new ArrayList<>();

    public InventoryManager() {
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerUseItemEvent.class, e -> {
            Player p = e.getPlayer();
            if (!e.getItemStack().isAir() && !playersCooldown.contains(p)) {
                cooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerUseItemOnBlockEvent.class, e -> {
            Player p = e.getPlayer();
            if (!e.getItemStack().isAir() && !playersCooldown.contains(p)) {
                cooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> {
            Player p = e.getPlayer();
            if (!p.getItemInMainHand().isAir() && !playersCooldown.contains(p)) {
                cooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });
    }


    public void assignInventory(Player p, InventoryEnum inventory) {
        changeInventory(p, inventory.getHotbarInventory());
    }

    private void changeInventory(Player p, HotbarInventory inventory) {
        p.getInventory().clear();
        inventory.getItems().forEach((itemStack -> p.getInventory().addItemStack(itemStack.getItem())));
        activeHotBar.put(p, inventory);
    }

    private void cooldown(Player p) {
        if (playersCooldown.contains(p)) return;
        playersCooldown.add(p);
        MinecraftServer.getSchedulerManager().buildTask(() -> playersCooldown.remove(p)).delay(300, ChronoUnit.MILLIS).schedule();
    }
}
