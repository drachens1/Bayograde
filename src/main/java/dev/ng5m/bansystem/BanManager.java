package dev.ng5m.bansystem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.ng5m.Util;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static org.drachens.util.PlayerUtil.getPlayerFromUUID;

public class BanManager {
    private Function<Player, Component> banMessage = p -> Component.text("You have been banned from the server.");
    private final File file;

    public void setBanMessage(Function<Player, Component> banMessage) {
        this.banMessage = banMessage;
    }

    public Function<Player, Component> getBanMessage() {
        return banMessage;
    }

    public BanManager(File file) {
        this.file = file;

        if (!file.exists()) {
            try {
                file.createNewFile();
                writeString("{}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JsonObject getRoot() {
        String read = Util.readFile(file.toPath());
        JsonElement parser = JsonParser.parseString(read);

        return parser.getAsJsonObject();
    }

    private void writeString(String s) {
        try {
            var writer = new FileWriter(file);
            writer.write(s);
            writer.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    private void write(JsonObject root) {
        var serialized = root.toString();

        writeString(serialized);
    }

    public void banPlayer(UUID player, long duration) {
        var root = removeEntry(player);

        root.add(player.toString(), new JsonPrimitive(System.currentTimeMillis() + duration));

        write(root);
        getPlayerFromUUID(player).kick("Banned");
    }

    public JsonObject removeEntry(UUID player) {
        var root = getRoot();

        if (root.has(player.toString()))
            root.remove(player.toString());

        write(root);
        return root;
    }

    public boolean isBanned(Player player) {
        var root = getRoot();

        if (!root.has(player.getUuid().toString()))
            return false;

        return System.currentTimeMillis() <= root.get(player.getUuid().toString()).getAsLong();
    }
}
