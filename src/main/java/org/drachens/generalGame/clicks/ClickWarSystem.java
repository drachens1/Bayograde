package org.drachens.generalGame.clicks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.War;
import org.drachens.player_types.CPlayer;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.min;
import static org.drachens.util.OtherUtil.bound;
import static org.drachens.util.ServerUtil.blockVecToPos;


public class ClickWarSystem implements War {
    private final CurrencyTypes cost = CurrencyEnum.production.getCurrencyType();
    final int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    public Province AdjacentBlocks(@NotNull Pos position, Country country, Instance instance) {
        int adjacentCount = 0;

        for (int[] direction : directions) {
            int offsetX = direction[0];
            int offsetY = direction[1];

            Province province = ContinentalManagers.world(instance).provinceManager().getProvince(position.add(offsetX, 0, offsetY));
            if (null != province && null != province.getOccupier() && (country.isMilitaryFriend(province.getOccupier())||country==province.getOccupier())) {
                adjacentCount++;
                if (3 <= adjacentCount) {
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
        if (null == country) return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (null == province || null == province.getOccupier() || !province.getOccupier().isAtWar(country.getName()))
            return;
        Payment payment = getCost(instance);
        if (!country.canMinusCost(payment)) {
            p.sendActionBar(Component.text("You cannot afford this", NamedTextColor.RED));
            return;
        }
        Province atkFrom = AdjacentBlocks(province.getPos(), country, instance);
        if (null == atkFrom) return;
        country.removePayment(payment);
        province.capture(atkFrom.getOccupier());
    }

    public Province canCapture(Province province, Country country){
        Payment payment = new Payment(cost, bound(1000f,1f, (float) ContinentalManagers.world(country.getInstance()).dataStorer().factoryCount / 100));
        if (null == province || null == province.getOccupier() || !province.getOccupier().isAtWar(country.getName()) || !country.canMinusCost(payment))
            return null;
        return AdjacentBlocks(province.getPos(), country, country.getInstance());
    }

    public Payment getCost(Instance instance){
        return new Payment(cost, (float) ContinentalManagers.world(instance).dataStorer().factoryCount / 100 + 1);
    }
}
