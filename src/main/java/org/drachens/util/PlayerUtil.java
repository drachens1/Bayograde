package org.drachens.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class PlayerUtil {
    private static final HashMap<Player, Country> playerCountryHashMap = new HashMap<>();
    private static final HashMap<Player, Player> lastMsg = new HashMap<>();

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

    public static Player getPlayerFromName(String name) {
        return MinecraftServer.getConnectionManager().findOnlinePlayer(name);
    }

    public static Country getCountryFromPlayer(Player p) {
        return playerCountryHashMap.get(p);
    }

    public static void addPlayerToCountryMap(Player p) {
        playerCountryHashMap.put(p, null);
    }

    public static void addPlayerToCountryMap(Player p, Country country) {
        playerCountryHashMap.put(p, country);
    }

    public static void removePlayerFromCountryMap(Player p) {
        playerCountryHashMap.remove(p);
    }

    public static Player getPlayersLastMessanger(Player p) {
        return lastMsg.get(p);
    }

    public static void setPlayersLastMessanger(Player p, Player ps) {
        lastMsg.put(p, ps);
        lastMsg.put(ps, p);
    }
}
