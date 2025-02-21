package org.drachens.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public enum PlayerUtil {
    ;

    public static UUID getUUIDFromName(String playerName) {
        try {
            URL url = URL.of(URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerName), null);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            String id = json.get("id").getAsString();

            return UUID.fromString(id.replaceFirst(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"
            ));
        } catch (Exception e) {
            System.err.println("UUID from name failed with " + playerName + ' ' + e.getMessage());
        }
        return null;
    }
}
