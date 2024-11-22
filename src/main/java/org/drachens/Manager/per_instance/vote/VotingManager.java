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
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;
import org.drachens.events.VoteEvent;
import org.drachens.interfaces.VotingOption;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.drachens.util.KyoriUtil.getPrefixes;
import static org.drachens.util.Messages.broadcast;

public class VotingManager {
    private final HashMap<VotingOption, List<Player>> votes = new HashMap<>();
    private final VoteBar voteBar;
    private final Instance instance;
    private boolean voted = false;
    private Task task;
    private VotingOption winner;

    public VotingManager(List<VotingOption> votingOptions, Instance instance) {
        for (VotingOption votingOption : votingOptions) {
            votes.put(votingOption, new ArrayList<>());
        }
        this.instance = instance;
        voteEventListener();
        voteBar = new VoteBar(instance);
        reset();
        MinecraftServer.getGlobalEventHandler().addListener(ResetEvent.class, e -> reset());
    }

    public void vote(VotingOption votingOption, Player p) {
        votes.get(votingOption).add(p);
        voted = true;
    }

    public VotingOption getWinner() {
        return winner;
    }

    public void setWinner() {
        winner = votes.keySet().stream().toList().getFirst();
        for (Map.Entry<VotingOption, List<Player>> e : votes.entrySet()) {
            if (votes.get(winner).size() < e.getValue().size()) {
                winner = e.getKey();
            }
        }
    }

    public void voteEventListener() {
        MinecraftServer.getGlobalEventHandler().addListener(VoteEvent.class, e -> {
            broadcast(Component.text()
                            .append(getPrefixes("vote"))
                            .append(e.getPlayer().getName())
                            .append(Component.text(" has voted for ", NamedTextColor.GREEN))
                            .append(Component.text(e.getVoted().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                            .build()
                    , e.getPlayer().getInstance()
            );
            ContinentalManagers.world(e.getPlayer().getInstance()).votingManager().vote(e.getVoted(), e.getPlayer());
        });
    }

    public VoteBar getVoteBar() {
        return voteBar;
    }

    public void reset() {
        voted = false;
        votes.forEach((voteOption, players) -> votes.put(voteOption, new ArrayList<>()));
        voteBar.start();
        if (task != null) task.cancel();
        task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!voted) {
                broadcast(Component.text()
                        .append(getPrefixes("vote"))
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
