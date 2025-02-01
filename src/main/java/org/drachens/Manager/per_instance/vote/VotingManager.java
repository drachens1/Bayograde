package org.drachens.Manager.per_instance.vote;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.VotingOption;
import org.drachens.events.VoteEvent;
import org.drachens.events.system.StartGameEvent;
import org.drachens.util.MessageEnum;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.drachens.util.Messages.broadcast;

public class VotingManager {
    private final HashMap<Player, VotingOption> votes = new HashMap<>();
    private final VoteBar voteBar;
    private final Instance instance;
    private boolean voted = false;
    private Task task;
    private VotingOption winner;

    public VotingManager(Instance instance) {
        this.instance = instance;
        voteEventListener();
        voteBar = new VoteBar(instance);
        reset();
    }

    public void vote(VotingOption votingOption, Player p) {
        votes.put(p,votingOption);
        voted = true;
    }

    public void setWinner() {
        int wc = 0;
        winner = null;
        HashMap<VotingOption, Integer> count = new HashMap<>();
        for (Map.Entry<Player, VotingOption> e : votes.entrySet()) {
            int c = count.getOrDefault(e.getValue(),0);
            c++;
            count.put(e.getValue(),c);
        }
        count.forEach((votingOption, integer) -> {
            if (wc<integer){
                winner=votingOption;
            }
        });
    }

    public void voteEventListener() {
        MinecraftServer.getGlobalEventHandler().addListener(VoteEvent.class, e -> {
            broadcast(Component.text()
                            .append(MessageEnum.vote.getComponent())
                            .append(e.p().getName())
                            .append(Component.text(" has voted for ", NamedTextColor.GREEN))
                            .append(Component.text(e.voted().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                            .build()
                    , e.p().getInstance()
            );
            ContinentalManagers.world(e.p().getInstance()).votingManager().vote(e.voted(), e.p());
        });
    }

    public VoteBar getVoteBar() {
        return voteBar;
    }

    public void reset() {
        voted = false;
        votes.clear();
        voteBar.start();
        if (task != null) task.cancel();
        task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!voted) {
                broadcast(Component.text()
                        .append(MessageEnum.vote.getComponent())
                        .append(Component.text("restarted due to no votes", NamedTextColor.GREEN))
                        .build(), instance);
                reset();
            } else {
                setWinner();
                EventDispatcher.call(new StartGameEvent(instance, winner));
                task.cancel();
            }
        }).delay(12, ChronoUnit.SECONDS).schedule();
    }
}
