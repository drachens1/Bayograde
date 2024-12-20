package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.events.demands.DemandAcceptedEvent;
import org.drachens.events.demands.DemandCounterOfferEvent;
import org.drachens.events.demands.DemandDeniedEvent;
import org.drachens.temporary.demand.WW2Demands;

import java.util.Objects;

import static org.drachens.Manager.defaults.ContinentalManagers.inventoryManager;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;
import static org.drachens.util.KyoriUtil.getPrefixes;

public class DemandIncomingCMD extends Command {
    public DemandIncomingCMD() {
        super("incoming");
        setCondition((sender, s) -> hasDemandSent(sender));

        var options = ArgumentType.String("Demands")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemandSent(sender)) return;
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getDemandCountryNames());
                });

        var choice = ArgumentType.String("choice")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemandSent(sender)) return;
                    CPlayer p = (CPlayer) sender;
                    if (!p.getCountry().getDemandCountryNames().contains(context.get(options))) return;
                    suggestion.addEntry(new SuggestionEntry("accept"));
                    suggestion.addEntry(new SuggestionEntry("deny"));
                    suggestion.addEntry(new SuggestionEntry("counter-offer"));
                    suggestion.addEntry(new SuggestionEntry("view"));
                });

        var third = ArgumentType.String("")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (Objects.equals(context.get(choice), "view")) {
                        suggestion.addEntry(new SuggestionEntry("off"));
                        suggestion.addEntry(new SuggestionEntry("on"));
                    }
                });

        DemandManager demandManager = ContinentalManagers.demandManager;
        addSyntax((sender, context) -> {
        }, options);

        Component notSent = Component.text()
                .append(getPrefixes("country"))
                .append(Component.text("That country hasn't sent you a demand and/or they no longer have a player controlling them"))
                .build();

        addSyntax((sender, context) -> {
            if (!hasDemandSent(sender)) return;
            CPlayer p = (CPlayer) sender;
            CPlayer player = (CPlayer) ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(options)).getPlayerLeader();
            if (player == null) {
                p.sendMessage(notSent);
                return;
            }

            Demand sentDemand = demandManager.getDemand(player);

            Country from = player.getCountry();
            Country to = p.getCountry();
            switch (context.get(choice)) {
                case "accept":
                    EventDispatcher.call(new DemandAcceptedEvent(sentDemand, from, to));
                    sentDemand.accepted();
                    break;
                case "deny":
                    EventDispatcher.call(new DemandDeniedEvent(sentDemand, from, to));
                    sentDemand.denied();
                    break;
                case "counter-offer":
                    Demand demand = new WW2Demands(sentDemand.getToCountry(), sentDemand.getFromCountry());
                    demand.copyButOpposite(sentDemand);
                    demandManager.addActive(p, demand);
                    inventoryManager.assignInventory(p, InventoryEnum.demand);
                    EventDispatcher.call(new DemandCounterOfferEvent(to, from));
                    break;
            }

        }, options, choice);
        addSyntax((sender, context) -> {
            if (!hasDemandSent(sender)) return;
            CPlayer p = (CPlayer) sender;
            CPlayer player = (CPlayer) ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(options)).getPlayerLeader();
            if (player == null) {
                p.sendMessage(notSent);
                return;
            }

            WW2Demands sentDemand = (WW2Demands) demandManager.getDemand(player);

            Country from = player.getCountry();
            Country to = p.getCountry();
            switch (context.get(choice)) {
                case "accept":
                    EventDispatcher.call(new DemandAcceptedEvent(sentDemand, from, to));
                    sentDemand.accepted();
                    break;
                case "deny":
                    EventDispatcher.call(new DemandDeniedEvent(sentDemand, from, to));
                    sentDemand.denied();
                    break;
                case "counter-offer":
                    Demand demand = new WW2Demands(sentDemand.getToCountry(), sentDemand.getFromCountry());
                    demand.copyButOpposite(sentDemand);
                    demandManager.addActive(p, demand);
                    inventoryManager.assignInventory(p, InventoryEnum.demand);
                    EventDispatcher.call(new DemandCounterOfferEvent(to, from));
                    break;
                case "view":
                    switch (context.get(third)) {
                        case "off":
                            sentDemand.hidePlayer(p);
                            break;
                        case "on":
                            sentDemand.showPlayer(p);
                            break;
                    }
                    break;
            }
        }, options, choice, third);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasDemandSent(CommandSender sender) {
        if (!isLeaderOfCountry(sender)) return false;
        CPlayer p = (CPlayer) sender;
        return p.getCountry().hasAnyDemands();
    }
}
