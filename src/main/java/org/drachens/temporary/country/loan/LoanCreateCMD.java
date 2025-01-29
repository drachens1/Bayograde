package org.drachens.temporary.country.loan;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.events.loan.LoanSendEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class LoanCreateCMD extends Command {
    public LoanCreateCMD() {
        super("create");
        CurrencyTypes production = CurrencyEnum.production.getCurrencyType();
        var amount = ArgumentType.Float("amount");
        var interest = ArgumentType.Float("interest 1-100");
        var termLength = ArgumentType.Integer("time-to-repay-days");

        var countries = getCountriesArgExcludingPlayersCountry();

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        addSyntax((sender, context) -> {
        }, countries);
        addSyntax((sender, context) -> {
        }, countries, amount);
        addSyntax((sender, context) -> {
        }, countries, interest);

        addSyntax(((sender, context) -> {
            if (!isLeaderOfCountry(sender)) {
                sender.sendMessage("You are not the leader of a country");
                return;
            }
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (target == null) return;
            float a = context.get(amount);
            float i = context.get(interest);
            int t = context.get(termLength);
            Loan loan = new Loan(a, production, i, t, country, target);
            target.addLoanRequest(loan);
            EventDispatcher.call(new LoanSendEvent(p.getInstance(), country, target, loan));
        }), countries, amount, interest, termLength);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
