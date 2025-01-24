package org.drachens.cmd.Dev.whitelist;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;

public class WhitelistCMD extends Command {
    public WhitelistCMD() {
        super("whitelist");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        addSubcommand(new WhitelistAddCMD());
        addSubcommand(new WhitelistRemoveCMD());
        addSubcommand(new WhitelistToggleCMD());
    }
}
