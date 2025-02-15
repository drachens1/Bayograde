package org.drachens.generalGame.country.diplomacy.demand;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.events.countries.demands.DemandAcceptedEvent;
import org.drachens.events.countries.demands.DemandCounterOfferEvent;
import org.drachens.events.countries.demands.DemandDeniedEvent;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;
import static org.drachens.util.Messages.sendMessage;

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

        var third = ArgumentType.String("view")
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
                .append(MessageEnum.country.getComponent())
                .append(Component.text("That country hasn't sent you a demand"))
                .build();

        addSyntax((sender, context) -> {
            if (!hasDemandSent(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country from = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(options));
            if (from == null) {
                sendMessage(p, notSent);
                return;
            }

            Demand sentDemand = p.getCountry().getDemand(from);
            if (sentDemand == null) {
                sendMessage(p, notSent);
                return;
            }

            Country to = p.getCountry();
            switch (context.get(choice)) {
                case "accept":
                    EventDispatcher.call(new DemandAcceptedEvent(sentDemand, from, to));
                    break;
                case "deny":
                    EventDispatcher.call(new DemandDeniedEvent(sentDemand, from, to));
                    sentDemand.denied();
                    break;
                case "counter-offer":
                    EventDispatcher.call(new DemandCounterOfferEvent(to, from, sentDemand));
                    break;
            }

        }, options, choice);

        addSyntax((sender, context) -> {
            if (!hasDemandSent(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country from = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(options));
            if (from == null) {
                sendMessage(p, notSent);
                return;
            }

            WW2Demands sentDemand = (WW2Demands) demandManager.getDemand(from);

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
                    EventDispatcher.call(new DemandCounterOfferEvent(to, from, sentDemand));
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

        setDefaultExecutor((sender, context) -> {
            if (!hasDemandSent(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            List<Component> comps = new ArrayList<>();
            country.getDemandCountryNames().forEach(name -> comps.add(Component.text("- " + name)));
            p.sendMessage(Component.text().append(comps).build());
        });
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
