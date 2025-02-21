package org.drachens.generalGame.country.diplomacy.demand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

public class DemandAddPaymentCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandAddPaymentCMD() {
        super("payment");

        Argument<String> types = ArgumentType.String("types")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemand(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("offer"));
                    suggestion.addEntry(new SuggestionEntry("demand"));
                });

        Argument<String> payments = ArgumentType.String("currency")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (hasDemand(sender)) {
                        suggestion.addEntry(new SuggestionEntry(CurrencyEnum.production.name()));
                    }
                });

        ArgumentFloat amount = ArgumentType.Float("Amount");

        Component currencyDoesntExist = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("That currency doesnt exist", NamedTextColor.RED))
                .build();

        Component currencyDoesExist = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("You have added this to demands successfully", NamedTextColor.GREEN))
                .build();

        setCondition((sender, s) -> hasDemand(sender));
        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            String choice = context.get(types);
            if (!("offer".equalsIgnoreCase(choice) || "demand".equalsIgnoreCase(choice))) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p.getCountry());
            CurrencyTypes currencyTypes = CurrencyEnum.valueOf(context.get(payments)).getCurrencyType();
            if (null == currencyTypes) {
                p.sendMessage(currencyDoesntExist);
                return;
            }
            Payment payment = new Payment(currencyTypes, context.get(amount));
            switch (choice) {
                case "offer":
                    demand.addPaymentOffer(payment);
                    break;
                case "demand":
                    demand.addPaymentDemand(payment);
                    break;
            }

            p.sendMessage(currencyDoesExist);
        }, types, payments, amount);
        addSyntax((sender, context) -> {
        }, types, payments);
        addSyntax((sender, context) -> {
        }, types);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return (null != country) && country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasDemand(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive(p.getCountry());
    }
}
