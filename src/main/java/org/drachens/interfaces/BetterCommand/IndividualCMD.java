package org.drachens.interfaces.BetterCommand;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IndividualCMD extends Command implements BetterCommandInterface {
    public IndividualCMD(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
    }
}
