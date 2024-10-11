package org.drachens.cmd.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;

public class Members extends Command {
    public Members() {
        super("members");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            if (!getCountryNames(p.getInstance()).contains(context.get(countries))) {
            }
        }, countries);
    }
}
