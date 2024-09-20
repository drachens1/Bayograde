package dev.ng5m;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Util {

    public static List<Player> getAllOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        MinecraftServer.getInstanceManager().getInstances()
                .forEach(i -> players.addAll(i.getPlayers()));
        return players;
    }

    public static String alnum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlNum(int length) {
        StringBuilder sb = new StringBuilder(length);

        var random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(alnum.charAt(random.nextInt(alnum.length())));
        }

        return sb.toString();
    }
}
