package org.drachens.temporary.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.temporary.country.diplomacy.DiplomacyCMD;
import org.drachens.temporary.country.edit.EditCMD;
import org.drachens.temporary.country.info.Info;
import org.drachens.temporary.country.loan.LoanCMD;
import org.drachens.temporary.country.manage.ManageCMD;
import org.drachens.temporary.country.modifiers.ModifiersCMD;
import org.drachens.temporary.country.puppets.PuppetCMD;
import org.drachens.temporary.country.research.ResearchCMD;

public class CountryCMD extends Command {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /country "));
        addSubcommand(new JoinCMD());
        addSubcommand(new Tp());
        addSubcommand(new Info());
        addSubcommand(new Leader());
        addSubcommand(new ManageCMD());
        addSubcommand(new AcceptCMD());
        addSubcommand(new DiplomacyCMD());
        addSubcommand(new PayCMD());
        addSubcommand(new LoanCMD());
        addSubcommand(new CountryChatCMD());
        addSubcommand(new PuppetCMD());
        addSubcommand(new EditCMD());
        addSubcommand(new ModifiersCMD());
        addSubcommand(new ResearchCMD());

        var smth = ArgumentType.String("type...")

                .setSuggestionCallback((sender, context, suggestion) -> {
                    Player p = (Player) sender;
                    p.refreshCommands();
                });
        addSyntax((sender, context) -> {
        }, smth);
    }
}
