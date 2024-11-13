package org.drachens.cmd.Dev.debug;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.Dev.debug.CountryTypes.CountryHistoryCMD;
import org.drachens.cmd.Dev.debug.CountryTypes.CountryTypesCMD;
import org.drachens.cmd.Dev.debug.countryDebug.CountryDebugCMD;

public class allCMD extends Command {
    public allCMD(String permission) {
        super("all");
        setCondition((sender, s) -> sender.hasPermission(permission));
        setDefaultExecutor((sender, context) -> {
            if (sender.hasPermission(permission)) sender.sendMessage("Proper usage /debug <option>");
        });
        addSubcommand(new CountryHistoryCMD(permission));
        addSubcommand(new CountryTypesCMD(permission));
        addSubcommand(new CountryDebugCMD(permission));
    }
}
