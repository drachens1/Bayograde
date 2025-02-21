package dev.ng5m;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public enum Util {
    ;
    public static final String alnum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static List<Player> getAllOnlinePlayers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream().toList();
    }

    public static String randomAlNum(int length) {
        StringBuilder sb = new StringBuilder(length);

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(alnum.charAt(random.nextInt(alnum.length())));
        }

        return sb.toString();
    }

    public static String readFile(Path path) {
        StringBuilder sb = new StringBuilder();

        try {
            List<@NotNull String> lines = Files.readAllLines(path);
            for (String line : lines) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static void writeString(File file, String s) {
        try {
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write(s);
            writer.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
