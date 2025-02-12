package org.drachens.cmd.settings.premium.autovote;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.player_types.CPlayer;

public class HelpAutoVoteCMD extends Command {
    public HelpAutoVoteCMD() {
        super("help");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasRank(RankEnum.deratus.getRank());
        });

    }
}
