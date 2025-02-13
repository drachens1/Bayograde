package org.drachens.cmd.settings.premium.suffix;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class SuffixCMD extends Command {
    public SuffixCMD() {
        super("suffix");

        setCondition(((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        }));

        addSubcommand(new HelpSuffixCMD());
        addSubcommand(new SetSuffixCMD());
    }
}
