package org.drachens.cmd.whitelist;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.WhitelistManager;

public class WhitelistCMD extends Command {
    public WhitelistCMD(WhitelistManager whitelistManager){
        super("whitelist");
        setCondition((sender,s)->sender.hasPermission("whitelist"));
        addSubcommand(new WhitelistAddCMD(whitelistManager));
        addSubcommand(new WhitelistRemoveCMD(whitelistManager));
        addSubcommand(new WhitelistToggleCMD(whitelistManager));
    }
}
