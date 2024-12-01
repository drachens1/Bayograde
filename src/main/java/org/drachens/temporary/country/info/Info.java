package org.drachens.temporary.country.info;

import net.minestom.server.command.builder.Command;

public class Info extends Command {
    public Info() {
        super("info");

        addSubcommand(new GeneralCMD());
        addSubcommand(new LoanInfoCMD());

    }
}
