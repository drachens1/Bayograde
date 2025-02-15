package org.drachens.generalGame.country.diplomacy.justifywar;

import net.minestom.server.command.builder.Command;

public class JustifyWarCMD extends Command {
    public JustifyWarCMD() {
        super("justify-war");

        addSubcommand(new JustifyAgainstCMD());
        addSubcommand(new JustifyCancelCMD());
        addSubcommand(new JustifyOptionsCMD());
    }
}
