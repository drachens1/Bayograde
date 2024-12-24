package org.drachens.temporary.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.temporary.country.diplomacy.DiplomacyCMD;
import org.drachens.temporary.country.info.AllInformationCMD;
import org.drachens.temporary.country.info.Info;
import org.drachens.temporary.country.loan.LoanCMD;
import org.drachens.temporary.country.manage.ManageCMD;

public class CountryCMD extends Command {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /country "));
        addSubcommand(new JoinCMD());
        addSubcommand(new Tp());
        addSubcommand(new Info());
        addSubcommand(new Leader());
        addSubcommand(new AllInformationCMD());
        addSubcommand(new ManageCMD());
        addSubcommand(new AcceptCMD());
        addSubcommand(new DiplomacyCMD());
        addSubcommand(new PayCMD());
        addSubcommand(new LoanCMD());

        var smth = ArgumentType.String("type...")

                .setSuggestionCallback((sender, context, suggestion) -> {
                    Player p = (Player) sender;
                    p.refreshCommands();
                });
        addSyntax((sender, context) -> {
        }, smth);
    }
}
