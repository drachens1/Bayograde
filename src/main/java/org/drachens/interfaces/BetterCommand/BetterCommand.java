package org.drachens.interfaces.BetterCommand;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BetterCommand extends Command implements BetterCommandInterface {
    public List<IndividualCMD> subCommands = new ArrayList<>();

    public BetterCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
        var logic = ArgumentType.String("");
        logic.setSuggestionCallback((sender, context, suggestion) -> {
            System.out.println("suggestion");
            if (!requirements(sender)) return;
            for (IndividualCMD command : subCommands) {
                if (command.requirements(sender)) {
                    System.out.println("add : "+command.getName());
                    addCommandSuggestions(command, suggestion);
                }
            }
        });
        logic.setDefaultValue("something");
        addSyntax((sender, context) -> {}, logic);
    }

    public void addCommand(IndividualCMD command) {
        subCommands.add(command);
        addSubcommand(command);
    }

    private void addCommandSuggestions(IndividualCMD command, Suggestion suggestion) {
        String commandName = command.getName();
        suggestion.addEntry(new SuggestionEntry(commandName));

        if (command.getAliases() != null)
            for (String alias : command.getAliases())
                if (alias!=null)
                    suggestion.addEntry(new SuggestionEntry(alias));
    }
}
