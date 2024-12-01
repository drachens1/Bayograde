package org.drachens.temporary.inventories.defaultinv;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class BuildItem2 extends HotbarItemButton {
    public BuildItem2() {
        super(10, itemBuilder(Material.CYAN_DYE, 11));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        e.getPlayer().sendMessage("ea");
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        e.getPlayer().sendMessage("cd");
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}
