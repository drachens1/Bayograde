package org.drachens.generalGame.country.edit.ideology;

import net.minestom.server.command.builder.Command;

public class IdeologiesCMD extends Command {
    public IdeologiesCMD() {
        super("ideologies");

        addSubcommand(new IdeologiesChangeOptionsCMD());
        addSubcommand(new IdeologiesBoostCMD());
    }


}
