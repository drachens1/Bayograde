package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.BetterCommand.BetterCommand;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class FactionCMD extends BetterCommand {
    public FactionCMD() {
        super("faction","f");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /faction "));
        addCommand(new JoinCMD());
        addCommand(new Create());
        addCommand(new Delete());
        addCommand(new Kick());
        addCommand(new Invite());
    }

    @Override
    public boolean requirements(CommandSender sender){
        if (!(sender instanceof Player p))return false;
        Country country = getCountryFromPlayer(p);
        if (country == null)return false;
        return true;
    }
}
