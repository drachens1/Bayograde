package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Delay;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.OtherUtil.runThread;

public class InventoryManager {
    private final HashMap<Player, HotbarInventory> activeHotBar = new HashMap<>();
    private final HashMap<Player, HotbarItemButton> lastButton = new HashMap<>();
    private final Delay delay = new Delay(200L);

    public InventoryManager() {

        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerUseItemEvent.class, e -> {
            Player p = e.getPlayer();
            if (!e.getItemStack().isAir() && !delay.hasCooldown(p)) {
                delay.startCooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerUseItemOnBlockEvent.class, e -> {
            Player p = e.getPlayer();
            if (!e.getItemStack().isAir() && !delay.hasCooldown(p)) {
                delay.startCooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerStartDiggingEvent.class, e -> {
            Player p = e.getPlayer();
            if (!p.getItemInMainHand().isAir() && !delay.hasCooldown(p)) {
                delay.startCooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerHandAnimationEvent.class, e -> {
            Player p = e.getPlayer();
            if (!p.getItemInMainHand().isAir() && !delay.hasCooldown(p)) {
                delay.startCooldown(p);
                activeHotBar.get(p).getItems().get(p.getHeldSlot()).onUse(e);
            }
        });

        globEHandler.addListener(PlayerChangeHeldSlotEvent.class, e->{
            Player p = e.getPlayer();
            HotbarItemButton last = lastButton.get(p);
            if (activeHotBar.containsKey(p)){
                List<HotbarItemButton> bs = activeHotBar.get(p).getItems();
                if (bs.size()<=e.getSlot()){
                    if (last!=null){
                        last.onSwapFrom(e);
                    }
                    return;
                }
                HotbarItemButton b = bs.get(e.getSlot());
                if (b==last)return;
                if (last!=null){
                    last.onSwapFrom(e);
                }
                b.onSwapTo(e);
                lastButton.put(p,b);
            }else {
                if (last!=null){
                    last.onSwapFrom(e);
                }
            }
        });

        globEHandler.addListener(PlayerMoveEvent.class, e->{
            Player p = e.getPlayer();
            if (!p.getItemInMainHand().isAir()) {
                runThread(()->activeHotBar.get(p).getItems().get(p.getHeldSlot()).onMove(e));
            }
        });

        globEHandler.addListener(ItemDropEvent.class, e-> e.setCancelled(true));
    }

    public void assignInventory(Player p, InventoryEnum inventory) {
        changeInventory(p, inventory.getHotbarInventory());
    }

    private void changeInventory(Player p, HotbarInventory inventory) {
        p.getInventory().clear();
        inventory.getItems().forEach((itemStack -> p.getInventory().addItemStack(itemStack.getItem())));
        activeHotBar.put(p, inventory);
    }
}
