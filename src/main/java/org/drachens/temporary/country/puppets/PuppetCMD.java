package org.drachens.temporary.country.puppets;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

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
