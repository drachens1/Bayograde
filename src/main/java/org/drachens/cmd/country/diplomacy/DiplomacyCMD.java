package org.drachens.cmd.country.diplomacy;

import net.minestom.server.command.builder.Command;

public class DiplomacyCMD extends Command {
    public DiplomacyCMD() {
        super("diplomacy");
        addSubcommand(new DemandCMD());
        addSubcommand(new JustifyWarCMD());
    }
}
