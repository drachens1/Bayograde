package dev.ng5m;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Util {

    public static String alnum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static List<Player> getAllOnlinePlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream().toList();
    }

    public static String randomAlNum(int length) {
        StringBuilder sb = new StringBuilder(length);

        var random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(alnum.charAt(random.nextInt(alnum.length())));
        }

        return sb.toString();
    }

    public static String readFile(Path path) {
        StringBuilder sb = new StringBuilder();

        try {
            var lines = Files.readAllLines(path);
            for (var line : lines) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static void writeString(File file, String s) {
        try {
            var writer = new FileWriter(file);
            writer.write(s);
            writer.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    public static Component colored(String text, Constants.Colors color) {
        return Component.text(text).color(color.color);
    }

    public static Class<?>[] toTypes(Object... o) {
        Class<?>[] arr = new Class<?>[o.length];

        for (int i = 0; i < o.length; i++) {
            arr[i] = o[i].getClass();
        }

        return arr;
    }

    public static void noop(Object... args) {

    }
}
