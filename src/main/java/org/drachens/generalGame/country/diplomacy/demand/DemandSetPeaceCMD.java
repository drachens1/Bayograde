package org.drachens.generalGame.country.diplomacy.demand;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;

public class DemandSetPeaceCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandSetPeaceCMD() {
        super("peace");
        ArgumentBoolean option = ArgumentType.Boolean("option");

        setCondition((sender, s) -> hasDemand(sender));

        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p.getCountry());
            demand.setPeace(context.get(option));
        }, option);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return (null != country) && country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasDemand(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive(p.getCountry());
    }
}
