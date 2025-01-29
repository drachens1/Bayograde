package org.drachens.temporary.troops.inventory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopMover extends HotbarItemButton {
    private final HashMap<Player, Troop> nextProv = new HashMap<>();

    public TroopMover() {
        super(itemBuilder(Material.CYAN_DYE, "Hi", NamedTextColor.GOLD));
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        Player p = e.getPlayer();
        if (province == null) return;
        if (nextProv.containsKey(p)) {
            Troop troop = nextProv.get(p);
            troop.move(province);
        }

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        Player p = e.getPlayer();
        if (province.getTroops().isEmpty()) return;
        Troop troop = province.getTroops().getFirst();
        if (nextProv.containsKey(p)) {
            nextProv.get(p).getTroop().setGlowing(false);
        }
        nextProv.put(p, troop);
        troop.getTroop().setGlowing(true);
    }
}
