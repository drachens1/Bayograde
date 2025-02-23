package org.drachens.Manager.decorational;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.player_types.CPlayer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TabManager {
    private final TextAnimation titleAnim = new TextAnimation(300L,Component.text()
            .append(Component.text("B", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("AYOGRADE",NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("B",NamedTextColor.GOLD)).append(Component.text("A",NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("YOGRADE",NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BA", NamedTextColor.GOLD)).append(Component.text("Y", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("OGRADE", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAY", NamedTextColor.GOLD)).append(Component.text("O", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("GRADE", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAYO", NamedTextColor.GOLD)).append(Component.text("G", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("RADE", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAYOG", NamedTextColor.GOLD)).append(Component.text("R", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("ADE", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAYOGR", NamedTextColor.GOLD)).append(Component.text("A", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("DE", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAYOGRA", NamedTextColor.GOLD)).append(Component.text("D", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text("E", NamedTextColor.GOLD)).build()
            ,Component.text().append(Component.text("BAYOGRAD", NamedTextColor.GOLD)).append(Component.text("E", NamedTextColor.GOLD, TextDecoration.BOLD)).build());

    private final TextAnimation timeAnim = new TextAnimation(300L,Component.text("A"),Component.text("B"),Component.text("C"));

    public TabManager(){
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class,e-> {
            Component footer = getFooter();
            Component header = getHeader();
            CPlayer p = (CPlayer) e.getPlayer();
            p.sendPlayerListHeaderAndFooter(header,footer);
        });

        MinecraftServer.getSchedulerManager().buildTask(()-> {
            Component footer = getFooter();
            Component header = getHeader();
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> player.sendPlayerListHeaderAndFooter(header,footer));
        }).repeat(300L,ChronoUnit.MILLIS).schedule();
    }

    public void updatePlayer(CPlayer p){
        PacketSendingUtils.sendGroupedPacket(p.getInstance().getPlayers(),new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                new PlayerInfoUpdatePacket.Entry(p.getUuid(),null,new ArrayList<>(),true,p.getLatency(),null,p.getFullPrefix(),null, 0)));
    }

    public PlayerInfoUpdatePacket getPacketForPlayer(CPlayer p){
        return  new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                new PlayerInfoUpdatePacket.Entry(p.getUuid(),null,new ArrayList<>(),true,p.getLatency(),null,p.getFullPrefix(),null, 0));
    }

    Component getHeader(){
        return Component.text()
                .append(titleAnim.getActive())
                .appendNewline()
                .append(Component.text("-----------",NamedTextColor.GRAY))
                .build();
    }

    Component getFooter(){
        return Component.text()
                .appendNewline()
                .append(Component.text("-----------",NamedTextColor.GRAY))
                    .build();
    }

    @Data
    static class TextAnimation {
        Component active;
        Task task;
        public TextAnimation(long delay, Component... components){
            active=components[0];
            task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
                int current = 0;
                @Override
                public void run() {
                    current++;
                    if (current==components.length){
                        current=0;
                    }
                    active = components[current];
                }
            }).repeat(delay, ChronoUnit.MILLIS).schedule();
        }
    }
}
