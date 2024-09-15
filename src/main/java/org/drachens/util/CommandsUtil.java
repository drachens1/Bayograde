package org.drachens.util;

import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.instance.Instance;

import java.util.List;
import java.util.stream.Collectors;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class CommandsUtil {
    public static Suggestion getCountriesAutoComplete(Suggestion suggestion, Instance instance) {
        for (String name : getWorldClasses(instance).countryDataManager().getNamesList()) {
            suggestion.addEntry(new SuggestionEntry(name));
        }
        return suggestion;
    }

    public static List<String> getCountryNames(Instance instance) {
        return getWorldClasses(instance).countryDataManager().getNamesList();
    }

    public static Suggestion suggestions(List<String> suggestion, Suggestion suggestions) {
        for (String s : suggestion) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            suggestions.addEntry(new SuggestionEntry(s));
        }
        return suggestions;
    }

    public static Suggestion getSuggestionsBasedOnInput(Suggestion suggestionss, String input, Instance i) {
        List<String> suggestions = getCountryNames(i);
        input = input.trim();
        if (input.isEmpty()) return suggestions(suggestions, suggestionss);
        System.out.println("suggestion input after and suggestions size:" + suggestions.size() + " Input length = " + input.length() + " input: " + input);
        String finalInput = input;
        return suggestions(suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase().startsWith(finalInput))
                .collect(Collectors.toList()), suggestionss);
    }
}