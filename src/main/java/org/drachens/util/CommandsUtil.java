package org.drachens.util;

import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.instance.Instance;

import java.util.List;
import java.util.stream.Collectors;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class CommandsUtil {

    public static List<String> getCountryNames(Instance instance) {
        return getWorldClasses(instance).countryDataManager().getNamesList();
    }

    public static List<String> getFactionNames(Instance instance) {
        return getWorldClasses(instance).countryDataManager().getFactionNames();
    }

    public static Suggestion suggestions(List<String> suggestion, Suggestion suggestions) {
        for (String s : suggestion) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            suggestions.addEntry(new SuggestionEntry(s));
        }
        return suggestions;
    }

    public static String cutInput(String input) {
        String[] inp = input.split(" ");
        return inp[inp.length-1];
    }

    public static Suggestion getSuggestionBasedOnInput(Suggestion suggestion, List<String> list) {
        String input = cutInput(suggestion.getInput());
        if (input.isEmpty()) return suggestions(list, suggestion);
        return suggestions(list.stream()
                .filter(suggestions -> suggestions.toLowerCase().startsWith(input))
                .collect(Collectors.toList()), suggestion);
    }
}