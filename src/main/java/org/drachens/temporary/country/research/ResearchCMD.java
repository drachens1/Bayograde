package org.drachens.temporary.country.research;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
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
        if (sender instanceof CPlayer p) {
            return p.getCountry() != null;
        }
        return true;
    }
}
