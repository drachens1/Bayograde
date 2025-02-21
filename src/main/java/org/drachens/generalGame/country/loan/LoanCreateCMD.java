package org.drachens.generalGame.country.loan;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.events.loan.LoanSendEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class LoanCreateCMD extends Command {
    public LoanCreateCMD() {
        super("create");
        CurrencyTypes production = CurrencyEnum.production.getCurrencyType();
        ArgumentFloat amount = ArgumentType.Float("amount");
        ArgumentFloat interest = ArgumentType.Float("interest 1-100");
        ArgumentInteger termLength = ArgumentType.Integer("time-to-repay-days");

        Argument<String> countries = getCountriesArgExcludingPlayersCountry();

        addSyntax((sender, context) -> {
        }, countries);
        addSyntax((sender, context) -> {
        }, countries, amount);
        addSyntax((sender, context) -> {
        }, countries, interest);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == target) return;
            float a = context.get(amount);
            float i = context.get(interest);
            int t = context.get(termLength);
            Loan loan = new Loan(a, production, i, t, country, target);
            target.getEconomy().addLoanRequest(country.getName(), loan);
            EventDispatcher.call(new LoanSendEvent(p.getInstance(), country, target, loan));
        }, countries, amount, interest, termLength);
    }
}
