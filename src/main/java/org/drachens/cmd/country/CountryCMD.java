package org.drachens.cmd.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.cmd.country.manage.ManageCMD;

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
        addSubcommand(new ManageCMD());

        var smth = ArgumentType.String("type...")

                .setSuggestionCallback((sender,context,suggestion)->{
                    Player p = (Player) sender;
                    p.refreshCommands();
                });
        addSyntax((sender,context)->{},smth);
    }
}
