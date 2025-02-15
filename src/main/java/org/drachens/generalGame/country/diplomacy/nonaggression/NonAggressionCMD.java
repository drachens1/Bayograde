package org.drachens.generalGame.country.diplomacy.nonaggression;

import net.minestom.server.command.builder.Command;

public class NonAggressionCMD extends Command {
    public NonAggressionCMD() {
        super("non-aggression-pact");

        addSubcommand(new NonAggressionCreateCMD());
        addSubcommand(new NonAggressionAcceptCMD());
    }
}
