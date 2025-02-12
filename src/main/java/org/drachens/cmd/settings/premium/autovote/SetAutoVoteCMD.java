package org.drachens.cmd.settings.premium.autovote;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class SetAutoVoteCMD extends Command {
    public SetAutoVoteCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("set");
        
        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasRank(RankEnum.deratus.getRank());
        });
        
        List<String> strings = new ArrayList<>();
        votingOptionsCMD.forEach(votingOption -> strings.add(votingOption.getName()));
        var votingOptions = ArgumentType.Word("voting")
                .from(strings.toArray(new String[0]));

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasRank(RankEnum.deratus.getRank()))return;
            p.sendMessage(Component.text()
                            .append(Component.text("Auto Vote set to: ", NamedTextColor.GREEN))
                            .append(Component.text(context.get(votingOptions)))
                    .build());
            p.getPlayerJson().setAutoVoteOption(context.get(votingOptions));
        },votingOptions);
    }
}
