package org.drachens.cmd.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.drachens.interfaces.BetterCommand.BetterCommand;

public class CountryCMD extends BetterCommand {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /country "));
        addCommand(new JoinCMD());
        addCommand(new Tp());
        addCommand(new Info());
        addCommand(new Members());
        addCommand(new Leader());
        addCommand(new AllInformationCMD());
        addCommand(new Extra());
    }

    @Override
    public boolean requirements(CommandSender sender) {
        if (sender instanceof Player) return true;
        return false;
    }
}
