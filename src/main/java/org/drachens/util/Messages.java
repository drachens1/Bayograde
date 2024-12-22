package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;

import dev.ng5m.CPlayer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Messages {
    public static void sendMessage(CPlayer p, Component msg){
        if (p!=null)p.sendMessage(msg);
    }

    public static void globalBroadcast(String msg) {
        logMsg("server", msg, null);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    public static void globalBroadcast(Component msg) {
        logMsg("server", msg, null);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

    public static void broadcast(String msg, Instance world) {
        logMsg("server", msg, world);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getInstance() == world) {
                p.sendMessage(msg);
            }
        }
    }

    public static void broadcast(Component msg, Instance world) {
        logMsg("server", msg, null);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (p.getInstance() == world) {
                p.sendMessage(msg);
            }
        }
    }

    public static void logCmd(String playerName, String cmd, Instance w) {
        System.out.println(playerName + " : " + cmd);
        try {
            FileWriter f = new FileWriter(ContinentalManagers.configFileManager.getLogCmds(), true);
            try {
                if (w != null) {
                    f.write(w.getDimensionName() + " " + getTime() + " " + playerName + ":" + cmd);
                } else {
                    f.write(getTime() + " " + playerName + ":" + cmd);
                }
                f.write("\n");
                f.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logMsg(String playerName, String msg, Instance w) {
        try {
            FileWriter f = new FileWriter(ContinentalManagers.configFileManager.getLogMsg(), true);
            try {
                if (w != null) {
                    f.write(w.getDimensionName() + " " + getTime() + " " + playerName + ":" + msg);
                } else {
                    f.write(getTime() + " " + playerName + ":" + msg);
                }
                f.write("\n");
                f.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logMsg(String playerName, Component msg, Instance w) {
        String msgS = PlainTextComponentSerializer.plainText().serialize(msg);
        System.out.println(playerName + " : " + msgS);
        try {
            FileWriter f = new FileWriter(ContinentalManagers.configFileManager.getLogMsg(), true);
            try {
                if (w != null) {
                    f.write(w.getDimensionName() + " " + getTime() + " " + playerName + ":" + msgS);
                } else {
                    f.write(getTime() + " " + playerName + ":" + msgS);
                }
                f.write("\n");
                f.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTime() {
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date());
    }
}
