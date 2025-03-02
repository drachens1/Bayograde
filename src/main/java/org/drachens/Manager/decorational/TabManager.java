package org.drachens.Manager.decorational;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.player_types.CPlayer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TabManager {
    private final Tag<Boolean> firstSpawnTag = Tag.Boolean("InstanceViewHook:FirstSpawn").defaultValue(true);
    private final TextAnimation titleAnim = new TextAnimation(500L,
            Component.text("Die!", NamedTextColor.GOLD),
            Component.text("KYS!", NamedTextColor.GREEN)
    );

    public TabManager() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            Instance instance = event.getInstance();

            player.sendPlayerListHeaderAndFooter(getHeader(), getFooter());

            PacketSendingUtils.sendGroupedPacket(instance.getPlayers(), getAddPlayerPacket(player), p -> p != player);

            if (player.getTag(firstSpawnTag)) {
                for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    if (onlinePlayer == player || onlinePlayer.getInstance() == instance) {
                        continue;
                    }
                    PacketSendingUtils.sendPacket(player, getRemovePlayerPacket(onlinePlayer));
                    PacketSendingUtils.sendPacket(onlinePlayer, getRemovePlayerPacket(player));
                }
                player.setTag(firstSpawnTag, false);
            } else {
                for (Player instancePlayer : instance.getPlayers()) {
                    if (instancePlayer != player) {
                        PacketSendingUtils.sendPacket(player, getAddPlayerPacket(instancePlayer));
                    }
                }
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(RemoveEntityFromInstanceEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) return;
            Instance instance = event.getInstance();

            PacketSendingUtils.sendGroupedPacket(instance.getPlayers(), getRemovePlayerPacket(player), p -> p != player);
            for (Player instancePlayer : instance.getPlayers()) {
                if (instancePlayer != player) {
                    PacketSendingUtils.sendPacket(player, getRemovePlayerPacket(instancePlayer));
                }
            }
        });

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Component footer = getFooter();
            Component header = getHeader();
            for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.sendPlayerListHeaderAndFooter(header, footer);
            }
        }).repeat(300L, ChronoUnit.MILLIS).schedule();
    }

    public void updatePlayer(CPlayer p) {
        PacketSendingUtils.sendGroupedPacket(
                p.getInstance().getPlayers(),
                getPacketForPlayer(p)
        );
    }

    public PlayerInfoUpdatePacket getPacketForPlayer(CPlayer p) {
        return new PlayerInfoUpdatePacket(
                PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                new PlayerInfoUpdatePacket.Entry(
                        p.getUuid(), null, new ArrayList<>(), true, p.getLatency(),
                        null, p.getFullPrefix(), null, 0
                )
        );
    }

    private Component getHeader() {
        return Component.text()
                .appendNewline()
                .appendNewline()
                .appendNewline()
                .appendNewline()
                .append(Component.text("\uD83E\uDD82"))
                .appendNewline()
                .append(Component.text("Beta Release",NamedTextColor.GOLD, TextDecoration.BOLD))
                .appendNewline()
                .build();
    }

    private Component getFooter() {
        return Component.text()
                .append(Component.text("Players online: ",NamedTextColor.GRAY))
                .append(Component.text(MinecraftServer.getConnectionManager().getOnlinePlayerCount(),NamedTextColor.GRAY))
                .build();
    }

    @Data
    static class TextAnimation {
        private Component active;
        private final Task task;

        public TextAnimation(long delay, Component... components) {
            this.active = components[0];
            this.task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
                int current = 0;

                @Override
                public void run() {
                    current = (current + 1) % components.length;
                    active = components[current];
                }
            }).repeat(delay, ChronoUnit.MILLIS).schedule();
        }
    }

    private ServerPacket getAddPlayerPacket(Player player) {
        List<PlayerInfoUpdatePacket.Property> properties = new ArrayList<>();
        if (player.getSkin() != null) {
            properties.add(new PlayerInfoUpdatePacket.Property(
                    "textures", player.getSkin().textures(), player.getSkin().signature()
            ));
        }

        return new PlayerInfoUpdatePacket(
                PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                new PlayerInfoUpdatePacket.Entry(
                        player.getUuid(), player.getUsername(), properties, true,
                        player.getLatency(), player.getGameMode(), player.getDisplayName(),
                        null, 0
                )
        );
    }

    private ServerPacket getRemovePlayerPacket(Player player) {
        return new PlayerInfoRemovePacket(player.getUuid());
    }
}
