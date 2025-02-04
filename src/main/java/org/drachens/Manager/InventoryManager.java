package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Delay;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.drachens.util.OtherUtil.runThread;

public class InventoryManager {
    private final HashMap<Player, HotbarInventory> activeHotBar = new HashMap<>();
    private final HashMap<Player, HotbarItemButton> activeButton = new HashMap<>();
    private final Delay delay = new Delay(200L);

    public InventoryManager() {
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        globEHandler.addListener(PlayerUseItemEvent.class, e ->
                runThread(()->handleButtonUse(new OnUse((CPlayer) e.getPlayer(),true,false,null,e.getInstance()))));

        globEHandler.addListener(PlayerUseItemOnBlockEvent.class, e ->
                runThread(()->handleButtonUse(new OnUse((CPlayer) e.getPlayer(),true,true,new Pos(e.getPosition()),e.getInstance()))));

        globEHandler.addListener(PlayerStartDiggingEvent.class, e ->
                runThread(()->handleButtonUse(new OnUse((CPlayer) e.getPlayer(),false,true,new Pos(e.getBlockPosition()),e.getInstance()))));

        globEHandler.addListener(PlayerChangeHeldSlotEvent.class,e-> runThread(()->handleSlotChange(e)));

        globEHandler.addListener(PlayerMoveEvent.class, e->runThread(()->handleMove(e)));

        globEHandler.addListener(ItemDropEvent.class, e-> e.setCancelled(true));
    }

    private void handleButtonUse(OnUse onUse) {
        Player player = onUse.player();
        activeButton.computeIfPresent(player, (p, button) -> {
            if (!delay.hasCooldown(p)) {
                delay.startCooldown(p);
                if (onUse.onBlock()){
                    if (onUse.rightClick()){
                        button.onRightClickOnBlock(onUse);
                    }else {
                        button.onLeftClickOnBlock(onUse);
                    }
                }else {
                    if (onUse.rightClick()){
                        button.onRightClick(onUse);
                    }else {
                        button.onLeftClick(onUse);
                    }
                }
            }
            return button;
        });
    }

    private void handleSlotChange(PlayerChangeHeldSlotEvent event) {
        Player player = event.getPlayer();

        HotbarItemButton lastButton = activeButton.getOrDefault(player, null);

        activeHotBar.computeIfPresent(player, (p, hotbar) -> {
            List<HotbarItemButton> buttons = hotbar.getItems();
            int slot = event.getSlot();

            if (slot >= buttons.size()) {
                if (lastButton != null) lastButton.onSwapFrom(event);
                activeButton.remove(player);
                return hotbar;
            }

            HotbarItemButton newButton = buttons.get(slot);
            if (newButton != lastButton) {
                if (lastButton != null) lastButton.onSwapFrom(event);
                newButton.onSwapTo(event);
                activeButton.put(player, newButton);
            }
            return hotbar;
        });
    }

    private void handleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        try {
            Optional.ofNullable(activeHotBar.get(player))
                    .map(hotbar -> hotbar.getItems().get(player.getHeldSlot()))
                    .ifPresent(button -> button.onMove(event));
        }catch (IndexOutOfBoundsException e){
            //
        }

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
