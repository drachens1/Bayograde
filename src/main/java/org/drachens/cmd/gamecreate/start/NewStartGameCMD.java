package org.drachens.cmd.gamecreate.start;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.cmd.vote.VotingOptionCMD;
import org.drachens.dataClasses.customgame.InterchangeWorld;
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
            new InterchangeWorld(p);
        },votingOptions,name);
    }
}
