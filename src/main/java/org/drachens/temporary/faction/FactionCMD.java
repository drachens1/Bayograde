package org.drachens.temporary.faction;

import net.minestom.server.command.builder.Command;
import org.drachens.temporary.faction.manage.ManageCMD;

public class FactionCMD extends Command {
    public FactionCMD() {
        super("faction");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction <command>"));

        addSubcommand(new JoinCMD());
        addSubcommand(new CreateCMD());
        addSubcommand(new LeaveCMD());
        addSubcommand(new InfoCMD());
        addSubcommand(new AcceptCMD());
        addSubcommand(new ManageCMD());
        addSubcommand(new FactionChatCMD());
        addSubcommand(new FactoryOptionsCMD());
    }
}
