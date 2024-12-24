package dev.ng5m;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import org.drachens.events.ranks.RankAddEvent;
import org.drachens.events.ranks.RankRemoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class Rank {
    public final Function<Player, Component> displayNameSupplier;
    public final Component prefix;
    public final Component suffix;
    public final NamedTextColor color;
    private final List<UUID> players = new ArrayList<>();
    private final String team;

    public Rank(Function<Player, Component> displayNameSupplier, Component prefix, Component suffix, NamedTextColor color, String name) {
        this.displayNameSupplier = displayNameSupplier;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.team = Util.randomAlNum(16);
    }

    public final ImmutableList<UUID> getPlayers() {
        return new ImmutableList<>(this.players);
    }

    protected final void sendPackets(String addedPlayer, Player player) {
        var conn = player.getPlayerConnection();

        conn.sendPacket(new TeamsPacket(this.team,
                new TeamsPacket.CreateTeamAction(
                        displayNameSupplier.apply(player), (byte) 0, TeamsPacket.NameTagVisibility.ALWAYS, TeamsPacket.CollisionRule.ALWAYS,
                        this.color, this.prefix, this.suffix, List.of()
                )));

        conn.sendPacket(new TeamsPacket(this.team,
                new TeamsPacket.AddEntitiesToTeamAction(
                        List.of(addedPlayer)
                )));
    }

    protected final void sendRemovePackets(Player player) {
        var conn = player.getPlayerConnection();

        conn.sendPacket(new TeamsPacket(this.team,
                new TeamsPacket.RemoveTeamAction()
        ));
    }

    // this always shows the rank globally
    public void addPlayer(Player player) {
        this.players.add(player.getUuid());

        sendPackets(player.getUsername(), player);
        for (var viewer : Util.getAllOnlinePlayers()) {
            sendPackets(player.getUsername(), viewer);
        }
        EventDispatcher.call(new RankAddEvent(player, this));
    }

    public void removePlayer(Player player) {
        Util.getAllOnlinePlayers().forEach(this::sendRemovePackets);

        this.players.remove(player.getUuid());
        EventDispatcher.call(new RankRemoveEvent(player, this));
    }
}
