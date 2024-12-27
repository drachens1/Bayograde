package org.drachens.temporary.clicks;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.War;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.ServerUtil.blockVecToPos;


public class ClickWarSystem implements War {
    private final CurrencyTypes hits = CurrencyEnum.production.getCurrencyType();
    private final Payment payment = new Payment(hits, 1);
    int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    private Province AdjacentBlocks(@NotNull Pos position, Country country, Instance instance) {
        int adjacentCount = 0;

        for (int[] direction : directions) {
            int offsetX = direction[0];
            int offsetY = direction[1];

            Province province = ContinentalManagers.world(instance).provinceManager().getProvince(position.add(offsetX, 0, offsetY));
            if (province.getOccupier()!=null && country.isMilitaryFriend(province.getOccupier())) {
                adjacentCount++;
                if (adjacentCount >= 3) {
                    return province;
                }
            }
        }
        return null;
    }

    @Override
    public void onClick(PlayerBlockInteractEvent e) {
    }

    @Override
    public void onClick(PlayerUseItemEvent e) {
    }

    @Override
    public void onClick(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (country == null) return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (province == null || province.getOccupier()==null || !province.getOccupier().isAtWar(country)) return;
        if (!country.canMinusCost(payment)) {
            p.sendActionBar(Component.text("You cannot afford this", NamedTextColor.RED));
            return;
        }
        Province atkFrom = AdjacentBlocks(province.getPos(), country, instance);
        if (atkFrom==null) return;
        country.removePayment(payment);
        province.capture(atkFrom.getOccupier());
    }
}
