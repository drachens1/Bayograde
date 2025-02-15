package org.drachens.generalGame.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

public class LeaveCMD extends Command {
    public LeaveCMD() {
        super("leave");
        setCondition(((sender, commandString) -> inCountry(sender)));

        setDefaultExecutor((sender,context)->{
            if (!inCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            country.removePlayer(p);
        });
    }

    private boolean inCountry(CommandSender sender){
        CPlayer p = (CPlayer) sender;
        return p.getCountry()!=null;
    }
}
