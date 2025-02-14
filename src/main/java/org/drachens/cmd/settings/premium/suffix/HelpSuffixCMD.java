package org.drachens.cmd.settings.premium.suffix;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class HelpSuffixCMD extends Command {
    public HelpSuffixCMD() {
        super("help");

        setCondition(((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        }));


    }
}
