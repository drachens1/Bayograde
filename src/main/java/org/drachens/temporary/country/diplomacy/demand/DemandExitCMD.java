package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.temporary.demand.WW2Demands;
import org.drachens.util.MessageEnum;

public class DemandExitCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandExitCMD() {
        super("cancel");
        Component exited = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("Exited demand mode", NamedTextColor.RED))
                .build();
        setCondition((sender, s) -> hasDemand(sender));
        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p.getCountry());
            demand.hidePlayer(p);
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
