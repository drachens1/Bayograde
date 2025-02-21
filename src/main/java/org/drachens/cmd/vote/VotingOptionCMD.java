package org.drachens.cmd.vote;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.VotingOption;
import org.drachens.events.VoteEvent;
import org.jetbrains.annotations.NotNull;

public class VotingOptionCMD extends Command {
    public VotingOptionCMD(@NotNull VotingOption votingOption) {
        super(votingOption.getName());
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)||!ContinentalManagers.generalManager.votingEnabled(p.getInstance()))
                return;
            if (!ContinentalManagers.world(p.getInstance()).getAsGlobalGameWorldClass().votingManager().getVoteBar().isShown()){
                p.sendMessage(Component.text("It is not a voting session", NamedTextColor.RED));
                return;
            }
            EventDispatcher.call(new VoteEvent(p, votingOption));
        });
    }
}
