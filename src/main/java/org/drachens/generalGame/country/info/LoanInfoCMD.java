package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class LoanInfoCMD extends Command {
    public LoanInfoCMD() {
        super("loan_info");
        setCondition(((sender, s) -> isLeaderOfCountry(sender)));
        setDefaultExecutor((sender, context) -> {
            if (!isLeaderOfCountry(sender)) {
                sender.sendMessage("You are not the leader of a country");
                return;
            }
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            List<Component> comps = new ArrayList<>();
            country.getVault().getLoans().forEach(loan -> comps.add(loan.getDescription()));
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
}
