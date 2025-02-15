package org.drachens.generalGame.country.puppets;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

public class PuppetCMD extends Command {
    public PuppetCMD() {
        super("puppet");
        setCondition((sender, s) -> isOrHasPuppets(sender));

        addSubcommand(new PuppetChatCMD());
    }

    private boolean isOrHasPuppets(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && (country.hasOverlord() || country.hasPuppets());
        }
        return false;
    }
}
