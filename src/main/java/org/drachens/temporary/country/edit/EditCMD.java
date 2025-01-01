package org.drachens.temporary.country.edit;

import net.minestom.server.command.builder.Command;
import org.drachens.temporary.country.edit.ideology.IdeologiesCMD;
import org.drachens.temporary.country.edit.laws.LawsCMD;

public class EditCMD extends Command {
    public EditCMD() {
        super("edit");
        addSubcommand(new LawsCMD());
        addSubcommand(new IdeologiesCMD());
        addSubcommand(new EditOptionsCMD());
    }
}
