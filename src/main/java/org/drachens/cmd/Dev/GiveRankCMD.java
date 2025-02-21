package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.player_types.CPlayer;

public class GiveRankCMD extends Command {
    public GiveRankCMD() {
        super("give-rank");

        ArgumentString ranks = ArgumentType.String("rank");

        addSyntax((sender, context) -> {
            RankEnum rank = RankEnum.valueOf(context.get(ranks));
            CPlayer p = (CPlayer) sender;
            rank.getRank().addPlayer(p);
        },ranks);
    }
}
