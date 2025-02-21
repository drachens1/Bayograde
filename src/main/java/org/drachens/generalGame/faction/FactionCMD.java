package org.drachens.generalGame.faction;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.generalGame.faction.manage.ManageCMD;
import org.drachens.player_types.CPlayer;

public class FactionCMD extends Command {
    public FactionCMD() {
        super("faction");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction <command>"));
        setCondition((sender, s)->{
            CPlayer p = (CPlayer) sender;
            return ContinentalManagers.generalManager.factionsEnabled(p.getInstance());
        });
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
