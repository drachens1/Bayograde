package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.Currencies;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.temporary.demand.WW2Demands;

import static org.drachens.util.KyoriUtil.getPrefixes;

public class DemandAddPaymentCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final Currencies currencies = ContinentalManagers.defaultsStorer.currencies;
    public DemandAddPaymentCMD() {
        super("payment");

        var types = ArgumentType.String("types")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemand(sender))return;
                    suggestion.addEntry(new SuggestionEntry("offer"));
                    suggestion.addEntry(new SuggestionEntry("demand"));
                });

        var payments = ArgumentType.String("currency")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (hasDemand(sender)) currencies.getCurrencyNames().forEach(s-> suggestion.addEntry(new SuggestionEntry(s)));
                });

        var amount = ArgumentType.Float("Amount");

        Component prefix = getPrefixes("country");
        if (prefix==null)return;
        Component currencyDoesntExist = Component.text()
                .append(prefix)
                .append(Component.text("That currency doesnt exist", NamedTextColor.RED))
                        .build();

        Component currencyDoesExist = Component.text()
                        .append(prefix)
                .append(Component.text("You have added this to demands successfully",NamedTextColor.GREEN))
                                .build();

        setCondition((sender,s)->hasDemand(sender));
        addSyntax((sender,context)->{
            if (!hasDemand(sender))return;
            String choice = context.get(types);
            if (!(choice.equalsIgnoreCase("offer")||choice.equalsIgnoreCase("demand")))return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p);
            CurrencyTypes currencyTypes = currencies.getCurrencyType(context.get(payments));
            if (currencyTypes==null){
                p.sendMessage(currencyDoesntExist);
                return;
            }
            Payment payment = new Payment(currencyTypes,context.get(amount));
            switch (choice){
                case "offer":
                    demand.addPaymentOffer(payment);
                    break;
                case "demand":
                    demand.addPaymentDemand(payment);
                    break;
            }

            p.sendMessage(currencyDoesExist);
        },types,payments,amount);
        addSyntax((sender,context)->{},types,payments);
        addSyntax((sender,context)->{},types);
    }
    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
    private boolean hasDemand(CommandSender sender){
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive((Player) sender);
    }
}
