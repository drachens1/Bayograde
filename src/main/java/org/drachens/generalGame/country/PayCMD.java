package org.drachens.generalGame.country;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.List;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class PayCMD extends Command {
    public PayCMD() {
        super("pay");
        setCondition((sender, s)-> isLeaderOfCountry(sender));

        Argument<String> countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!isLeaderOfCountry(sender)) {
                        return;
                    }
                    CPlayer p = (CPlayer) sender;
                    List<String> countries1 = getCountryNames(p.getInstance());
                    countries1.remove(p.getCountry().getName());
                    getSuggestionBasedOnInput(suggestion, countries1);
                });

        ArgumentFloat amount = ArgumentType.Float("amount");
        CurrencyTypes production = CurrencyEnum.production.getCurrencyType();

        addSyntax((sender, context) -> {
        }, countries);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country to = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == to) return;
            Country from = p.getCountry();
            float value = context.get(amount);
            Payment payment = new Payment(production, value);
            if (from.canMinusCost(payment)) {
                to.addPayment(payment, Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(from.getComponentName())
                        .append(Component.text(" has sent you "))
                        .append(Component.text(value))
                        .append(production.getSymbol())
                        .build());
                from.sendMessage(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(Component.text("You have sent"))
                        .append(to.getComponentName())
                        .append(Component.text(" "))
                        .append(Component.text(value))
                        .build());
            }
        }, countries, amount);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return (null != country) && country.isPlayerLeader(p);
        }
        return false;
    }
}
