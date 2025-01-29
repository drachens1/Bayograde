package org.drachens.util;

import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.instance.Instance;
import org.drachens.player_types.CPlayer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class CommandsUtil {
    public static Argument<String> getCountriesArg() {
        return ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
                });
    }

    public static Argument<String> getCountriesArgExcludingPlayersCountry() {
        return ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    List<String> s = getCountryNames(p.getInstance());
                    s.remove(p.getCountry().getName());
                    getSuggestionBasedOnInput(suggestion, s);
                });
    }

    public static List<String> getCountryNames(Instance instance) {
        return getWorldClasses(instance).countryDataManager().getNamesList();
    }

    public static List<String> getFactionNames(Instance instance) {
        return getWorldClasses(instance).countryDataManager().getFactionNames();
    }

    public static Suggestion suggestions(List<String> suggestion, Suggestion suggestions) {
        suggestion.forEach(s -> suggestions.addEntry(new SuggestionEntry(s)));
        return suggestions;
    }

    public static String cutInput(String input) {
        String[] inp = input.split(" ");
        return inp[inp.length - 1];
    }

    public static Suggestion getSuggestionBasedOnInput(Suggestion suggestion, List<String> list) {
        String input = cutInput(suggestion.getInput());
        if (input.endsWith("\0")) return suggestions(list, suggestion);
        return suggestions(list.stream()
                .filter(suggestions -> suggestions.toLowerCase().startsWith(input))
                .collect(Collectors.toList()), suggestion);
    }

    public static Suggestion suggestions(Set<String> suggestion, Suggestion suggestions) {
        suggestion.forEach(s -> suggestions.addEntry(new SuggestionEntry(s)));
        return suggestions;
    }

    public static Suggestion getSuggestionBasedOnInput(Suggestion suggestion, Set<String> list) {
        String input = cutInput(suggestion.getInput());
        if (input.endsWith("\0")) return suggestions(list, suggestion);
        return suggestions(list.stream()
                .filter(suggestions -> suggestions.toLowerCase().startsWith(input))
                .collect(Collectors.toList()), suggestion);
    }
}