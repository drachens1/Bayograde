package org.drachens.generalGame.country.edit.laws;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.laws.Law;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class LawsChangeCMD extends Command {
    public LawsChangeCMD() {
        super("change");

        var law2 = ArgumentType.String("categories")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getLawNames());
                });

        var options = ArgumentType.String("options")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    LawCategory law = p.getCountry().getLaw(context.get(law2));
                    if (law == null) {
                        suggestion.addEntry(new SuggestionEntry("Invalid category inputted"));
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, law.getLaws());
                });

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (country == null) {
                p.sendMessage(Component.text("Join a country in order to execute this command", NamedTextColor.RED));
                return;
            }
            if (!country.isPlayerLeader(p)) {
                p.sendMessage(Component.text("You are not the leader of a country", NamedTextColor.RED));
                return;
            }
            p.sendMessage(Component.text("Proper usage: /country laws change-options <laws>", NamedTextColor.RED));
        });

        addSyntax((sender, context) -> {
        }, law2);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            LawCategory lawCategory = country.getLaw(context.get(law2));
            if (lawCategory == null) {
                p.sendMessage(Component.text("That law category is null", NamedTextColor.RED));
                return;
            }
            Law law = lawCategory.getLaw(context.get(options));
            if (law == null) {
                p.sendMessage(Component.text("That law is null", NamedTextColor.RED));
                return;
            }
            lawCategory.setCurrent(law);
            p.sendMessage(Component.text()
                    .append(Component.text("Changed the law to "))
                    .append(law.modifier().getName())
                    .build());
        }, law2, options);
    }
}
