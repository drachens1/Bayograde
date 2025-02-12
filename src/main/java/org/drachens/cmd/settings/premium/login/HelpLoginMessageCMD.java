package org.drachens.cmd.settings.premium.login;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class HelpLoginMessageCMD extends Command {
    public HelpLoginMessageCMD() {
        super("help");

        setCondition(((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        }));
    }
}
