package org.drachens.cmd.vote;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.VoteEvent;
import org.drachens.interfaces.VotingOption;
import org.jetbrains.annotations.NotNull;

public class VotingOptionCMD extends Command {
    public VotingOptionCMD(@NotNull VotingOption votingOption) {
        super(votingOption.getName());
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)) return;
            EventDispatcher.call(new VoteEvent(p, votingOption));
        });
    }
}
