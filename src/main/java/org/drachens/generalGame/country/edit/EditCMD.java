package org.drachens.generalGame.country.edit;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.generalGame.country.edit.ideology.IdeologiesCMD;
import org.drachens.generalGame.country.edit.laws.LawsCMD;
import org.drachens.player_types.CPlayer;

public class EditCMD extends Command {
    public EditCMD() {
        super("edit");
        setCondition((sender,s)->isLeaderOfCountry(sender));
        addSubcommand(new LawsCMD());
        addSubcommand(new IdeologiesCMD());
        addSubcommand(new EditOptionsCMD());
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
