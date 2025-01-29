package org.drachens.cmd.ai;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.clicks.ClicksAI;

public class AICmd extends Command {
    public AICmd() {
        super("ai");

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ClicksAI clicksAI = (ClicksAI) ContinentalManagers.centralAIManager.getAIManagerFor(p.getInstance());
            clicksAI.createAIForCountry(country);
        });
    }
}
