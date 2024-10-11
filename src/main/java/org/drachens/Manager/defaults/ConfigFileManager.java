package org.drachens.Manager.defaults;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.drachens.cmd.Dev.whitelist.Whitelist;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.drachens.util.ServerUtil.start;

public class ConfigFileManager {
    private  final HashMap<String, ConfigurationNode> playerDataNodes = new HashMap<>(); // UUID as a string for the name
    private  final HashMap<String, YamlConfigurationLoader> playerDataLoaders = new HashMap<>(); // contains all the players perms as UUID's for the name of the files
    private  final HashMap<String, File> playerDataFiles = new HashMap<>();
    private  ConfigurationNode whitelistListNode;
    private  YamlConfigurationLoader whitelistLoader;    // contains all the whitelisted peoples as UUID's
    private  ConfigurationNode permissionsNode;
    private  YamlConfigurationLoader permissionsLoader;  // Has the permission groups that are created
    private  String logMsg;
    private  String logCmds;
    private  File msg;
    private  File cmd;
    private  Whitelist whitelist;

    public  void startup() {

        File playerData = new File("playerData");//Creates the parent directory
        playerData.mkdir();

        for (File f : Objects.requireNonNull(playerData.listFiles())) {
            playerDataFiles.put(f.getName(), f);
            playerDataLoaders.put(f.getName(), YamlConfigurationLoader.builder()
                    .file(f)
                    .build());
        }

        for (Map.Entry<String, YamlConfigurationLoader> e : playerDataLoaders.entrySet()) {
            try {
                playerDataNodes.put(playerDataFiles.get(e.getKey()).getName(), e.getValue().load());
            } catch (IOException err) {
                System.err.println("Unable to load operator loader " + err.getMessage());
            }
        }

        //To setup all the log stuff
        System.out.println("Creating logs");
        new File("logs").mkdir();
        new File("logs/cmds").mkdir();
        new File("logs/msg").mkdir();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy.HH.mm");
        logCmds = "logs/cmds/" + format.format(new Date()) + ".txt";
        cmd = new File(logCmds);
        fileExists(cmd);
        logMsg = "logs/msg/" + format.format(new Date()) + ".txt";
        msg = new File(logMsg);
        fileExists(msg);
        System.out.println("Logs finished");

        //Permissions
        File permissions = new File("permissions.yml");
        fileExists(permissions);
        permissionsLoader = YamlConfigurationLoader.builder()
                .file(permissions)
                .build();
        try {
            permissionsNode = permissionsLoader.load();
        } catch (ConfigurateException e) {
            System.err.println("Unable to load permissions " + e.getMessage());
        }
        loadWhitelist();
        start();
    }

    private  void loadWhitelist() {
        System.out.println("Whitelist started loading...");
        File whitelistFile = new File("whitelist.yml");
        fileExists(whitelistFile);
        whitelistLoader = YamlConfigurationLoader.builder()
                .file(whitelistFile)
                .build();
        try {
            whitelistListNode = whitelistLoader.load();
        } catch (ConfigurateException e) {
            System.out.println("Whitelist failed loading " + e.getMessage());
        }
        ConfigurationNode whitelisted = whitelistListNode.node("whitelist");
        ConfigurationNode actives = whitelisted.node("active");
        ConfigurationNode player = whitelisted.node("players");

        boolean active = actives.getBoolean();
        try {
            List<String> players = new ArrayList<>(Objects.requireNonNull(player.getList(String.class)));
            List<UUID> playerID = new ArrayList<>();
            for (String s : players) {
                playerID.add(UUID.fromString(s));
            }
            whitelist = new Whitelist(playerID, active);
            return;
        } catch (SerializationException e) {
            System.out.println("Players loading error " + e.getMessage());
        }
        whitelist = new Whitelist(new ArrayList<>(), false);
    }

    public  ConfigurationNode getPlayersData(UUID player) {//The string should be a UUID
        String playerName = player.toString() + ".yml";
        if (!playerDataNodes.containsKey(playerName)) {
            return createPlayersData(playerName);
        }
        return playerDataNodes.get(playerName);
    }

    public  YamlConfigurationLoader getPlayerDataLoader(UUID player) {
        String playerName = player.toString() + ".yml";
        if (!playerDataLoaders.containsKey(playerName)) {
            createPlayersData(playerName);
            return playerDataLoaders.get(playerName);
        }
        return playerDataLoaders.get(playerName);
    }

    public  ConfigurationNode createPlayersData(String player) {
        File playerData = new File("playerData/" + player);
        fileExists(playerData);
        System.out.println("creating player data file! " + player + " Name: " + playerData.getName());
        YamlConfigurationLoader temp = YamlConfigurationLoader.builder()
                .file(playerData)
                .build();
        playerDataLoaders.put(playerData.getName(), temp);
        playerDataFiles.put(playerData.getName(), playerData);

        try {
            ConfigurationNode c = temp.load();
            playerDataNodes.put(playerData.getName(), c);
            //Creates the default info
            ConfigurationNode general = c.node("general");
            general.node("name").set(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(UUID.fromString(player.replace(".yml", ""))));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
            general.node("joined").set(format.format(new Date()));
            general.node("lastOnline").set(format.format(new Date()));
            playerDataLoaders.get(playerData.getName()).save(playerDataNodes.get(playerData.getName()));
            return c;
        } catch (IOException e) {
            System.err.println("Unable to create the player datas " + e.getMessage());
            return null;
        }
    }

    private  void fileExists(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                System.out.println(f.getName() + " was created");
            } catch (IOException e) {
                //don't need anything here
            }
        }
    }

    public  void playerSave(UUID player) {
        String playerName = player.toString();
        if (!playerDataNodes.containsKey(playerName)) {
            return;
        }
        try {
            playerDataLoaders.get(playerName).save(playerDataNodes.get(playerName));
        } catch (ConfigurateException e) {
            System.err.println("Unable to save player data of player " + player + " error msg : " + e.getMessage());
        }
    }

    public  String getLogMsg() {
        return logMsg;
    }

    public  String getLogCmds() {
        return logCmds;
    }

    public  void loadPermissions(Player p) {
        UUID playerID = p.getUuid();
        ConfigurationNode wanted = getPlayersData(playerID);
        ConfigurationNode permissions = wanted.node("permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            if (perms.contains("operator")) {
                ContinentalManagers.permissions.playerOp(p);
            }
        } catch (SerializationException e) {
            System.out.println("Player operator failed " + p.getUsername() + " " + e.getMessage());
        }
    }

    public  void shutdown() {
        Scanner cmds = new Scanner(logCmds);
        Scanner log = new Scanner(logMsg);
        if (!cmds.hasNextLine()) cmd.delete();
        if (!log.hasNextLine()) msg.delete();
    }

    public  Whitelist getWhitelist() {
        return whitelist;
    }

    public  ConfigurationNode getWhitelistListNode() {
        return whitelistListNode;
    }

    public  void specificSave(String choice) {
        System.out.println("Specifically saving: " + choice);
        switch (choice) {
            case "whitelist":
                try {
                    whitelistLoader.save(whitelistListNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving whitelist: " + e.getMessage());
                }
                break;
            case "permissions":
                try {
                    permissionsLoader.save(permissionsNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving permissions: " + e.getMessage());
                }
        }
    }

    public  ConfigurationNode getPermissionsFile() {
        return permissionsNode;
    }
}
