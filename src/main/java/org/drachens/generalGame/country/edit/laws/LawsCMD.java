package org.drachens.generalGame.country.edit.laws;

import net.minestom.server.command.builder.Command;
import org.drachens.generalGame.country.info.LawsInfo;

public class LawsCMD extends Command {
    public LawsCMD() {
        super("laws");

        addSubcommand(new LawsInfo("info"));
        addSubcommand(new LawsChangeOptionsCMD());
        addSubcommand(new LawsChangeCMD());
    }
}
