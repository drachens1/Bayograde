package org.drachens.generalGame.country.research;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class ResearchCMD extends Command {
    public ResearchCMD() {
        super("research");

        setCondition((sender, s) -> inCountry(sender));

        addSubcommand(new ResearchOptionCMD());
        addSubcommand(new ResearchOptionsCMD());
        addSubcommand(new ResearchAfterCMD());
        addSubcommand(new ResearchActiveCMD());
        addSubcommand(new ResearchTechTreeCMD());
    }

    private boolean inCountry(CommandSender sender) {
        return !(sender instanceof CPlayer p) || ((null != p.getCountry()) && ContinentalManagers.generalManager.researchEnabled(p.getInstance()));
    }
}
