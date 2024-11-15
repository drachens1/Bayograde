package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.KyoriUtil.getPrefixes;

public class DemandCompleteCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    public DemandCompleteCMD() {
        super("complete");
        Component prefix = getPrefixes("country");
        if (prefix==null)return;
        Component exited = Component.text()
                .append(prefix)
                .append(Component.text("Completed the demand", NamedTextColor.GREEN))
                .build();
        setCondition((sender,s)->hasDemand(sender));
        addSyntax((sender,context)->{
            if (!hasDemand(sender))return;
            CPlayer p = (CPlayer) sender;
            demandManager.getDemand(p).complete();
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
    private boolean hasDemand(CommandSender sender){
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive((Player) sender);
    }
}
