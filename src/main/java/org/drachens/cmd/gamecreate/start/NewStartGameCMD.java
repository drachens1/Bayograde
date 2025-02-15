package org.drachens.cmd.gamecreate.start;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.dataClasses.customgame.CustomGameWorld;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class NewStartGameCMD extends Command {
    public NewStartGameCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("new");

        List<String> strings = new ArrayList<>();
        votingOptionsCMD.forEach(votingOption -> strings.add(votingOption.getName()));
        var votingOptions = ArgumentType.Word("voting")
                .from(strings.toArray(new String[0]));

        var name = ArgumentType.Word("name");

        addSyntax((sender,context)->{},votingOptions);

        addSyntax((sender,context)->{
            CPlayer p = (CPlayer) sender;
            if (!strings.contains(context.get(votingOptions))){
                p.sendMessage(Component.text("Not a valid option"));
                return;
            }
            new CustomGameWorld(p,VotingWinner.valueOf(context.get(votingOptions)).getVotingOption());
        },votingOptions,name);
    }
}
