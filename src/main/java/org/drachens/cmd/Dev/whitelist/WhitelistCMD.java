package org.drachens.cmd.Dev.whitelist;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

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
