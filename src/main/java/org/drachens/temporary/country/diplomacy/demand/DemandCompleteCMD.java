package org.drachens.temporary.country.diplomacy.demand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

public class DemandCompleteCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandCompleteCMD() {
        super("complete");
        Component exited = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("Completed the demand", NamedTextColor.GREEN))
                .build();
        setCondition((sender, s) -> hasDemand(sender));
        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            demandManager.getDemand(p.getCountry()).runCompleteEvent();
            demandManager.removeActive(p.getCountry());
            p.sendMessage(exited);
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

    private boolean hasDemand(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive(p.getCountry());
    }
}
