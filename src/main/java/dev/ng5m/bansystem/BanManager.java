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

public class BanManager {
    private final File file;
    private Function<Player, Component> banMessage = p -> Component.text("You have been banned from the server.");

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

    public Function<Player, Component> getBanMessage() {
        return banMessage;
    }

    public void setBanMessage(Function<Player, Component> banMessage) {
        this.banMessage = banMessage;
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

    public void banPlayer(UUID uuid, long duration) {
        var root = removeEntry(uuid);

        root.add(uuid.toString(), new JsonPrimitive(System.currentTimeMillis() + duration));

        write(root);
    }

    public JsonObject removeEntry(UUID uuid) {
        var root = getRoot();

        if (root.has(uuid.toString()))
            root.remove(uuid.toString());

        write(root);
        return root;
    }

    public boolean isBanned(UUID player) {
        var root = getRoot();

        if (!root.has(player.toString()))
            return false;

        return System.currentTimeMillis() <= root.get(player.toString()).getAsLong();
    }
}
