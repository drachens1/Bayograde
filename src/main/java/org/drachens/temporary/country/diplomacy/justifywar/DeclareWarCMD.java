package org.drachens.temporary.country.diplomacy.justifywar;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;


public class DeclareWarCMD extends Command {
    public DeclareWarCMD() {
        super("declare_war");
        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof Player p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
                });

        addSyntax((sender,context)->{},countries);
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
