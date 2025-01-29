package org.drachens.temporary.country.loan;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.Country;
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
        setCondition((sender, s) -> isLeaderOfCountry(sender));
        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!isLeaderOfCountry(sender)) {
                        return;
                    }
                    CPlayer p = (CPlayer) sender;
                    List<String> countries1 = new ArrayList<>();
                    p.getCountry().getLoanRequests().forEach((country, loan) -> countries1.add(country));
                    getSuggestionBasedOnInput(suggestion, countries1);
                });
        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            String countryName = context.get(countries);
            if (!country.getLoanRequests().containsKey(countryName)) {
                p.sendMessage(noLoanFound);
                return;
            }
            country.acceptLoan(countryName);
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
}
