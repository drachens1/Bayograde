package org.drachens.cmd.Dev.whitelist;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.customTypes.WhitelistFile;

import static org.drachens.util.Messages.globalBroadcast;

public class WhitelistToggleCMD extends Command {
    public WhitelistToggleCMD() {
        super("on", "off");
        WhitelistFile whitelistFile = ContinentalManagers.configFileManager.getWhitelistFile();

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        setDefaultExecutor((sender, context) -> {
            globalBroadcast(context.getInput());
            switch (context.getInput()) {
                case "whitelist on":
                    whitelistFile.toggle(true);
                    break;
                case "whitelist off":
                    whitelistFile.toggle(false);
                    break;
            }
        });
    }
}
