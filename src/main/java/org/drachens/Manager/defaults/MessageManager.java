package org.drachens.Manager.defaults;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;

import static org.drachens.util.KyoriUtil.getPrefixes;
import static org.drachens.util.Messages.broadcast;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.ServerUtil.cooldown;
import static org.drachens.util.ServerUtil.playerHasCooldown;

public class MessageManager {
    private final Component neutralComponent;
    private final Component gameOver;
    public MessageManager(){
        neutralComponent = Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Neutral", NamedTextColor.GOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .append(Component.text("Leader: \n"))
                .build();

        gameOver = Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("Game Over",NamedTextColor.GREEN))
                .build();

        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();

        globEHandler.addListener(StartGameEvent.class, e->{
            e.getVotingOption().getMapGenerator().generate(e.getInstance(),e.getVotingOption());
            broadcast(Component.text()
                    .append(getPrefixes("system"))
                    .append(Component.text(e.getVotingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" has won!!", NamedTextColor.GREEN))
                    .build(),e.getInstance());
        });

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Province p = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(new Pos(e.getBlockPosition()));
            if (p==null)globalBroadcast("NULLl");
            if (!e.getPlayer().isSneaking()||playerHasCooldown(e.getPlayer())||!p.isCapturable())return;
            cooldown(e.getPlayer());
            if (p.getOccupier() == null) {
                e.getPlayer().sendMessage(neutralComponent);
                return;
            }
            Country c = p.getOccupier();
            e.getPlayer().sendMessage(c.getDescription());
        });

        globEHandler.addListener(ResetEvent.class,e->broadcast(gameOver,e.getInstance()));
    }
}
