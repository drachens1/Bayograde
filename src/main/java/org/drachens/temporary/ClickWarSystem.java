package org.drachens.temporary;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.interfaces.War;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;
import static org.drachens.util.ServerUtil.blockVecToPos;


public class ClickWarSystem implements War {
    int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };
    private boolean AdjacentBlocks(@NotNull Pos position, Country country, Instance instance) {
        int adjacentCount = 0;

        for (int[] direction : directions) {
            int offsetX = direction[0];
            int offsetY = direction[1];

            Pos neighborLocation = position.add(offsetX, 0, offsetY);
            if (ContinentalManagers.world(instance).provinceManager().getProvince(neighborLocation).getOccupier()==country) {
                adjacentCount++;
                if (adjacentCount >= 3) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onClick(PlayerBlockInteractEvent e) {}

    @Override
    public void onClick(PlayerUseItemEvent e) {}

    @Override
    public void onClick(PlayerStartDiggingEvent e) {
        Player p = e.getPlayer();
        Country country = getCountryFromPlayer(p);
        if (country==null)return;
        Instance instance = e.getInstance();
        Province province = ContinentalManagers.world(instance).provinceManager().getProvince(blockVecToPos(e.getBlockPosition()));
        if (province==null || province.getOccupier()==country)return;
        CurrencyTypes hits = ContinentalManagers.defaultsStorer.currencies.getCurrencyType("production");
        if (!country.canMinusCost(new Payment(hits,1))){
            p.sendActionBar(compBuild("You cannot afford this", NamedTextColor.RED));
            return;
        }
        if (!AdjacentBlocks(province.getPos(),country,instance))return;
        country.addPayment(new Payment(hits,1));
        province.setOccupier(country);
    }
}
