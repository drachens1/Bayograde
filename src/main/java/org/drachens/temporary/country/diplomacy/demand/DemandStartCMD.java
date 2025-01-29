package org.drachens.temporary.country.diplomacy.demand;

import org.drachens.player_types.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.temporary.demand.WW2Demands;
import org.drachens.util.MessageEnum;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class DemandStartCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandStartCMD() {
        super("start");
        setCondition((sender, s) -> valid(sender));

        DemandManager demandManager = ContinentalManagers.demandManager;

        var countries = getCountriesArgExcludingPlayersCountry();

        Component doesntExist = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("This country does not exist", NamedTextColor.RED))
                .build();
        InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

        addSyntax((sender, context) -> {
            if (!valid(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country from = p.getCountry();
            Country to = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (to == null) {
                p.sendMessage(doesntExist);
                return;
            }
            Demand demand = new WW2Demands(from, to);
            p.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have started creating a demand against "))
                    .append(to.getNameComponent())
                    .build());
            demandManager.addActive(from, demand);
            inventoryManager.assignInventory(p, InventoryEnum.demand);
        }, countries);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean valid(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return isLeaderOfCountry(sender) && !demandManager.isPlayerActive(p.getCountry());
    }
}
