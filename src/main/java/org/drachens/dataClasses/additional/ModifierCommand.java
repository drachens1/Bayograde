package org.drachens.dataClasses.additional;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.suggestion.Suggestion;

public interface ModifierCommand {
    void getSuggestion(CPlayer p, CommandContext context, Suggestion suggestion);

    String getString();

    void execute(CPlayer p, String input);

    void properUsage(CPlayer p, CommandContext context);
}
