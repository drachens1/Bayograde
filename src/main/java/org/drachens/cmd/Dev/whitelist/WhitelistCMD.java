package org.drachens.cmd.Dev.whitelist;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.WhitelistManager;

public class WhitelistCMD extends Command {
    public WhitelistCMD(WhitelistManager whitelistManager) {
        super("whitelist");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        addSubcommand(new WhitelistAddCMD(whitelistManager));
        addSubcommand(new WhitelistRemoveCMD(whitelistManager));
        addSubcommand(new WhitelistToggleCMD(whitelistManager));
    }
}
