package org.drachens.temporary.country.info;

import net.minestom.server.command.builder.Command;

public class Info extends Command {
    public Info() {
        super("info");

        addSubcommand(new GeneralCMD());
        addSubcommand(new LoanInfoCMD());
        addSubcommand(new WarsInfoCMD());
        addSubcommand(new PuppetsCMD());
        addSubcommand(new LawsInfo("laws"));
        addSubcommand(new BordersInfoCMD());
        addSubcommand(new InfoOptionsCMD());
        addSubcommand(new Leader());
    }
}
