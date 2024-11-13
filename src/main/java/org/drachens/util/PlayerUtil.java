package org.drachens.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerUtil {
    public static Player getOnlinePlayerFromName(String name) {
        return MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(name);
    }

    public static UUID getUUIDFromName(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            String id = json.get("id").getAsString();

            return UUID.fromString(id.replaceFirst(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"
            ));
        } catch (Exception e) {
            System.err.println("UUID from name failed with " + playerName + " " + e.getMessage());
        }
        return null;
    }

    public static Player getPlayerFromUUID(UUID player) {
        return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player);
    }
}
