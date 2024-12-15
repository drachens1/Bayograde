package org.drachens.temporary.country.info;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;

public class LoanInfoCMD extends Command {
    public LoanInfoCMD() {
        super("loan_info");
        setCondition(((sender, s) -> isLeaderOfCountry(sender)));
        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
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
