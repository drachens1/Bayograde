package org.drachens.generalGame.country.loan;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class LoanAcceptCMD extends Command {
    public LoanAcceptCMD() {
        super("accept");
        Component noLoanFound = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("You don't have a loan request from that country"))
                .build();
        Argument<String> countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    List<String> countries1 = new ArrayList<>();
                    p.getCountry().getEconomy().getLoanRequests().forEach((country, loan) -> countries1.add(country));
                    getSuggestionBasedOnInput(suggestion, countries1);
                });
        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            String countryName = context.get(countries);
            if (!country.getEconomy().getLoanRequests().containsKey(countryName)) {
                p.sendMessage(noLoanFound);
                return;
            }
            country.getEconomy().getVault().addLoan(country.getEconomy().getLoan(countryName));
        }, countries);
    }
}
