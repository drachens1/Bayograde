package org.drachens.cmd.faction;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;

public class Delete {
    public Delete() {
        var logic = ArgumentType.String("Countries");
        logic.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
        });

        /*setCondition((sender,s)->{
            if (!(sender instanceof Player p))return false;
            Country country = getCountryFromPlayer(p);
            if (country == null)return false;
            return country.isLeaderOfAFaction();
        });*/
    }

    public void causes(CommandSender sender, String input) {

    }

    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof CPlayer p)) return false;
        Country country = p.getCountry();
        if (country == null) return false;
        return country.isLeaderOfAFaction();
    }

    public List<String> generateAutoComp(CommandSender sender) {
        return null;
    }
}
