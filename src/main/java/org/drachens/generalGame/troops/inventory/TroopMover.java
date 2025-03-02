package org.drachens.generalGame.troops.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopMover extends HotbarItemButton {
    private final HashMap<Player, List<Troop>> nextProv = new HashMap<>();

    public TroopMover() {
        super(itemBuilder(Material.CYAN_DYE, "Troop Mover", NamedTextColor.GOLD));
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (null == province) return;
        if (nextProv.containsKey(p)) {
            p.sendActionBar(Component.text("Moved"));
            List<Troop> troops = nextProv.get(p);
            if (troops.isEmpty()) {
                p.sendActionBar(Component.text("empty"));
            }
            List<Troop> toRemove = new ArrayList<>();
            troops.forEach(troop -> {
                if (troop.isDead()){
                    toRemove.add(troop);
                    return;
                }
                troop.move(province);
            });
            troops.removeAll(toRemove);
            nextProv.put(p,troops);
        }
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (province.getTroops().isEmpty()) return;
        List<Troop> selected = new ArrayList<>(province.getTroops());
        p.sendActionBar(Component.text("Selected"));
        if (p.isSneaking()){
            List<Troop> troops = nextProv.getOrDefault(p, new ArrayList<>());
            troops.addAll(selected);
            troops.forEach(troop -> troop.getTroop().setGlowingForPlayer(true,p,1424689));
            nextProv.put(p,troops);
        }else {
            if (nextProv.containsKey(p)) {
                List<Troop> troops = nextProv.getOrDefault(p, new ArrayList<>());
                troops.forEach(troop1 -> troop1.getTroop().setGlowingForPlayer(false,p,1424689));
            }
            selected.forEach(troop -> troop.getTroop().setGlowingForPlayer(true,p,1424689));
            nextProv.put(p, selected);
        }
    }
}
