package org.drachens.temporary.clicks;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.temporary.troops.TroopAI;

public class TmpCMD extends Command {//todo remove this
    public TmpCMD() {
        super("ai");

        setDefaultExecutor(((commandSender, commandContext) -> {
            CPlayer p = (CPlayer) commandSender;

            Country country = p.getCountry();

            switch (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner){
                case ww2_clicks -> new ClicksAI().createAIForCountry(country);
                case ww2_troops -> new TroopAI().createAIForCountry(country);
            }
        }));
    }
}
