package org.drachens.store.other;

import dev.ng5m.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.events.ranks.RankAddEvent;
import org.drachens.events.ranks.RankRemoveEvent;
import org.drachens.player_types.CPlayer;

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
    private final List<String> items;
    private final String identifier;
    private final int weight;

    public Rank(Create c){
        this.displayNameSupplier = c.displayNameSupplier;
        this.prefix = c.prefix;
        this.suffix = c.suffix;
        this.color = c.color;
        this.team = Util.randomAlNum(16);
        this.items =c.items;
        this.identifier =c.identifier;
        this.weight =c.weight;
    }

    public String toString(){
        return "[identifier:"+ identifier +"-weight:"+ weight + ']';
    }

    public String getIdentifier(){
        return identifier;
    }

    public int getWeight(){
        return weight;
    }

    public final ArrayList<UUID> getPlayers() {
        return new ArrayList<>(this.players);
    }

    protected final void sendPackets(String addedPlayer, Player player) {
        PlayerConnection conn = player.getPlayerConnection();

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
        PlayerConnection conn = player.getPlayerConnection();

        conn.sendPacket(new TeamsPacket(this.team,
                new TeamsPacket.RemoveTeamAction()
        ));
    }

    // this always shows the rank globally
    public void addPlayer(CPlayer player) {
        this.players.add(player.getUuid());
        player.addRank(this);

        sendPackets(player.getUsername(), player);
        for (Player viewer : Util.getAllOnlinePlayers()) {
            sendPackets(player.getUsername(), viewer);
        }
        EventDispatcher.call(new RankAddEvent(player, this));
    }

    public void removePlayer(CPlayer player) {
        Util.getAllOnlinePlayers().forEach(this::sendRemovePackets);
        player.removeRank(this);

        this.players.remove(player.getUuid());
        EventDispatcher.call(new RankRemoveEvent(player, this));
    }

    public List<String> getCosmetics(){
        return items;
    }

    public static class Create {
        public final Function<Player, Component> displayNameSupplier;
        public final Component prefix;
        public final Component suffix;
        public final NamedTextColor color;
        private final List<String> items = new ArrayList<>();
        private final String identifier;
        private final int weight;

        public Create(Function<Player, Component> displayNameSupplier, Component prefix, Component suffix, NamedTextColor color, String identifier, int weight) {
            this.displayNameSupplier = displayNameSupplier;
            this.prefix = prefix;
            this.suffix = suffix;
            this.color = color;
            this.identifier=identifier;
            this.weight=weight;
        }

        public Create addCosmetic(String storeItem){
            items.add(storeItem);
            return this;
        }

        public Rank build(){
            return new Rank(this);
        }
    }
}
