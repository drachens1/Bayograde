package org.drachens.store.other;

import org.drachens.player_types.CPlayer;
import dev.ng5m.ImmutableList;
import dev.ng5m.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import org.drachens.Manager.defaults.enums.LoginMessageEnum;
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
    private final List<LoginMessage> loginMessages;
    private final List<String> items;
    private final String identifier;

    public Rank(Create c){
        this.displayNameSupplier = c.displayNameSupplier;
        this.prefix = c.prefix;
        this.suffix = c.suffix;
        this.color = c.color;
        this.team = Util.randomAlNum(16);
        this.items=c.items;
        this.loginMessages=c.loginMessages;
        this.identifier=c.identifier;
    }

    public String getIdentifier(){
        return identifier;
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
    public void addPlayer(CPlayer player) {
        this.players.add(player.getUuid());
        player.addRank(this);

        sendPackets(player.getUsername(), player);
        for (var viewer : Util.getAllOnlinePlayers()) {
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

    public List<LoginMessage> getLoginMessages(){
        return loginMessages;
    }

    public List<String> getCosmetics(){
        return items;
    }

    public static class Create {
        public final Function<Player, Component> displayNameSupplier;
        public final Component prefix;
        public final Component suffix;
        public final NamedTextColor color;
        private final List<LoginMessage> loginMessages = new ArrayList<>();
        private final List<String> items = new ArrayList<>();
        private final String identifier;

        public Create(Function<Player, Component> displayNameSupplier, Component prefix, Component suffix, NamedTextColor color, String identifier) {
            this.displayNameSupplier = displayNameSupplier;
            this.prefix = prefix;
            this.suffix = suffix;
            this.color = color;
            this.identifier=identifier;
        }

        public Create addLoginMessage(LoginMessageEnum loginMessageEnum){
            loginMessages.add(loginMessageEnum.getLoginMessage());
            return this;
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
