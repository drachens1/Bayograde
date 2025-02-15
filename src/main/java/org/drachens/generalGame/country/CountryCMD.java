package org.drachens.generalGame.country;

import net.minestom.server.command.builder.Command;
import org.drachens.generalGame.country.diplomacy.DiplomacyCMD;
import org.drachens.generalGame.country.edit.EditCMD;
import org.drachens.generalGame.country.info.Info;
import org.drachens.generalGame.country.loan.LoanCMD;
import org.drachens.generalGame.country.manage.ManageCMD;
import org.drachens.generalGame.country.manage.modifiers.ModifiersCMD;
import org.drachens.generalGame.country.puppets.PuppetCMD;
import org.drachens.generalGame.country.research.ResearchCMD;

public class CountryCMD extends Command {
    public CountryCMD() {
        super("country", "c");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /country "));
        addSubcommand(new JoinCMD());
        addSubcommand(new Tp());
        addSubcommand(new Info());
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
        addSubcommand(new LeaveCMD());
    }
}
