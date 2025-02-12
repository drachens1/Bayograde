package org.drachens.Manager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChatEvent;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.AdminMessageType;
import org.drachens.player_types.CPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class ChatCensor {
    private final HashSet<Pattern> bannedPatterns = new HashSet<>();

    private final Pattern EMOJI_PATTERN = Pattern.compile(
            "[\\p{So}\\p{Cn}]",
            Pattern.UNICODE_CASE
    );

    public ChatCensor() {
        loadFile();
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            String msg = normalize(event.getRawMessage());
            System.out.println(event.getRawMessage());
            if (containsEmoji(msg)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("Emojis are not allowed!");
                return;
            }
            for (Pattern pattern : bannedPatterns) {
                if (pattern.matcher(msg).find()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Component.text("Failed sending message", NamedTextColor.RED));
                    ContinentalManagers.adminManager.broadcast(AdminMessageType.swears,Component.text(msg));
                    return;
                }
            }
        });
    }

    public boolean isOkay(String msg){
        msg = normalize(msg);
        if (containsEmoji(msg)) {
            return false;
        }
        for (Pattern pattern : bannedPatterns) {
            if (pattern.matcher(msg).find()) {
                ContinentalManagers.adminManager.broadcast(AdminMessageType.swears,Component.text(msg));
                return false;
            }
        }
        return true;
    }

    private void loadFile() {
        File f = new File("bad-words.txt");
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    System.out.println(f.getName() + " was created.");
                }
            } catch (IOException e) {
                System.err.println("Error creating bad-words.txt: " + e.getMessage());
            }
        }
        try {
            List<String> words = Files.readAllLines(f.toPath());
            addBannedWords(words);
        } catch (IOException e) {
            System.err.println("Error reading bad-words.txt: " + e.getMessage());
        }
    }

    private boolean containsEmoji(String input) {
        return EMOJI_PATTERN.matcher(input).find();
    }

    public void addBannedWords(List<String> words) {
        words.forEach(word -> {
            String regex = createRegexPattern(word);
            if (regex.isBlank())return;
            bannedPatterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
        });
    }

    private String normalize(String input) {
        if (input == null) return "";
        input = input.toLowerCase()
                .replaceAll("0", "o")
                .replaceAll("1", "i")
                .replaceAll("3", "e")
                .replaceAll("4", "a")
                .replaceAll("@", "a")
                .replaceAll("\\$", "s")
                .replaceAll("5", "s")
                .replaceAll("7", "t")
                .replaceAll("8", "b");
        input = input.replaceAll("[^a-z ]", "");
        return input;
    }

    private String createRegexPattern(String word) {
        StringBuilder pattern = new StringBuilder();
        for (char c : normalize(word).toCharArray()) {
            pattern.append(c).append("+\\W*");
        }

        return pattern.toString();
    }
}
