package dev.ng5m;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class Rank {
    private final List<UUID> players = new ArrayList<>();

    public final Function<Player, Component> displayNameSupplier;
    public final Component prefix;
    public final Component suffix;
    public final NamedTextColor color;
    private final String team;

    public Rank(Function<Player, Component> displayNameSupplier, Component prefix, Component suffix, NamedTextColor color) {
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
    }

    public void removePlayer(Player player) {
        Util.getAllOnlinePlayers().forEach(this::sendRemovePackets);

        this.players.remove(player.getUuid());
    }


}
