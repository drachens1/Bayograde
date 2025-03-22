package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public enum Messages {
    ;

    public static final Logger LOGGER = Logger.getLogger("bayograde");
    public static void sendMessage(CPlayer p, Component msg) {
        if (null != p) p.sendMessage(msg);
    }

    public static void globalBroadcast(String msg) {
        LOGGER.info("server "+msg);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    public static void globalBroadcast(Component msg) {
        LOGGER.info("server " + msg);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    public static void broadcast(String msg, Instance world) {
        LOGGER.info("server " + msg +" : "+ world.getDimensionName());
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getInstance() == world) {
                p.sendMessage(msg);
            }
        }
    }

    public static void broadcast(Component msg, Instance world) {
        LOGGER.info("server "+ msg);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getInstance() == world) {
                p.sendMessage(msg);
            }
        }
    }


    public static void broadcast(CPlayer sender, Component msg, Instance world) {
        LOGGER.info(sender.getUsername()+" : "+msg);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getInstance() == world) {
                p.sendMessage(msg);
            }
        }
    }

    public static String getTime() {
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date());
    }
}
