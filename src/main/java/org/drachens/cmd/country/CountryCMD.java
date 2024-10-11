package org.drachens.cmd.country;

import net.minestom.server.command.builder.Command;

public class CountryCMD extends Command {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /<join|info|members|leader> <country>"));
        addSubcommand(new JoinCMD());
        addSubcommand(new Tp());
        addSubcommand(new Info());
        addSubcommand(new Members());
        addSubcommand(new Leader());
        addSubcommand(new AllInformationCMD());
    }
}
