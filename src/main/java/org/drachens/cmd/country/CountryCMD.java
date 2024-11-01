package org.drachens.cmd.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class CountryCMD extends Command {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /country "));
        addSubcommand(new JoinCMD());
        addSubcommand(new Tp());
        addSubcommand(new Info());
        addSubcommand(new Members());
        addSubcommand(new Leader());
        addSubcommand(new AllInformationCMD());
        addSubcommand(new Extra());
    }

    public boolean requirements(CommandSender sender) {
        if (sender instanceof Player) return true;
        return false;
    }
}
