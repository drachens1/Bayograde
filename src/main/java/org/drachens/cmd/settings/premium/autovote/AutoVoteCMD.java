package org.drachens.cmd.settings.premium.autovote;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.player_types.CPlayer;

import java.util.List;

public class AutoVoteCMD extends Command {
    public AutoVoteCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("auto-vote");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasRank(RankEnum.deratus.getRank());
        });

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasRank(RankEnum.deratus.getRank()))return;
            p.sendMessage(Component.text()
                            .append(Component.text("Auto Vote: ",NamedTextColor.GREEN))
                            .append(Component.text(p.getPlayerJson().getAutoVoteOption()))
                            .appendNewline()
                            .append(Component.text("Active: ", NamedTextColor.GREEN))
                            .append(Component.text(p.getPlayerJson().isAutoVoteActive()))
                    .build());
        }));

        addSubcommand(new SetAutoVoteCMD(votingOptionsCMD));
        addSubcommand(new HelpAutoVoteCMD());
        addSubcommand(new ToggleActiveAutoVoteCMD());
    }
}
