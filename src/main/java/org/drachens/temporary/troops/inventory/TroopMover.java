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
import org.drachens.temporary.troops.buildings.Barracks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopMover extends HotbarItemButton {
    private final HashMap<Player, List<Troop>> nextProv = new HashMap<>();

    public TroopMover() {
        super(itemBuilder(Material.CYAN_DYE, "Hi", NamedTextColor.GOLD));
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        Player p = e.getPlayer();
        if (province == null) return;
        if (nextProv.containsKey(p)) {
            List<Troop> troops = nextProv.getOrDefault(p, new ArrayList<>());
            troops.forEach(troop -> troop.move(province));
        }
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        Player p = e.getPlayer();
        if (province.getTroops().isEmpty()) return;
        List<Troop> selected = province.getTroops();
        if (p.isSneaking()){
            List<Troop> troops = nextProv.getOrDefault(p, new ArrayList<>());
            troops.addAll(selected);
            troops.forEach(troop -> troop.getTroop().setGlowing(true));
            nextProv.put(p,troops);
        }else {
            if (nextProv.containsKey(p)) {
                List<Troop> troops = nextProv.getOrDefault(p, new ArrayList<>());
                troops.forEach(troop1 -> troop1.getTroop().setGlowing(false));
            }
            selected.forEach(troop -> troop.getTroop().setGlowing(true));
            nextProv.put(p, selected);
        }

    }
}
