package org.drachens.dataClasses.additional;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class GreatDepression implements ModifierCommand {
    private final Component properUsage = Component.text("Proper usage /country modifiers depression ", NamedTextColor.RED);
    @Override
    public void getSuggestion(CPlayer p, CommandContext context, Suggestion suggestion) {
        suggestion.addEntry(new SuggestionEntry("protectionism"));
        suggestion.addEntry(new SuggestionEntry("devalue-currency"));
        suggestion.addEntry(new SuggestionEntry("abandon-gold-standard"));
        suggestion.addEntry(new SuggestionEntry("relief"));
        suggestion.addEntry(new SuggestionEntry("recovery"));
        suggestion.addEntry(new SuggestionEntry("reform"));
        suggestion.addEntry(new SuggestionEntry("what-it-do"));
    }

    @Override
    public String getString() {
        return "depression";
    }

    @Override
    public void execute(CPlayer p, String input) {
        switch (input){
            case "protectionism":

                break;
            case "devalue-currency":

                break;
            case "abandon-gold-standard":

                break;
            case "relief":

                break;
            case "recovery":

                break;
            case "reform":

                break;
            case "what-it-do":

                break;
        }
    }

    @Override
    public void properUsage(CPlayer p, CommandContext context) {
        p.sendMessage(properUsage);
    }


}
