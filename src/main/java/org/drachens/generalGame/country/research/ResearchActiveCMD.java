package org.drachens.generalGame.country.research;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

public class ResearchActiveCMD extends Command {
    public ResearchActiveCMD() {
        super("currently-active");

        setCondition((sender, s) -> !notCountry(sender));
        setDefaultExecutor((sender, context) -> {
            if (notCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            p.sendMessage(country.getResearch().researchCountry().getCurrentResearch().getName());
        });
    }

    private boolean notCountry(CommandSender sender) {
        return !(sender instanceof CPlayer p) || (null == p.getCountry());
    }
}
