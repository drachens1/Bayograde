package org.drachens.cmd.Dev.whitelist;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.WhitelistManager;

import static org.drachens.util.Messages.globalBroadcast;

public class WhitelistToggleCMD extends Command {
    public WhitelistToggleCMD(WhitelistManager whitelistManager) {
        super("on", "off");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        setDefaultExecutor((sender, context) -> {
            globalBroadcast(context.getInput());
            switch (context.getInput()) {
                case "whitelist on":
                    whitelistManager.toggle(true);
                    break;
                case "whitelist off":
                    whitelistManager.toggle(false);
                    break;
            }
        });
    }
}
