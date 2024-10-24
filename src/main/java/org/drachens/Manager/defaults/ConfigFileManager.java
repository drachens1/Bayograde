package org.drachens.Manager.defaults;

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

import static org.drachens.util.Messages.getTime;
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
    private File serverProperties;
    private ConfigurationNode propertiesConfigurationNode;
    private YamlConfigurationLoader propertiesLoader;

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

        serverProperties = new File("serverProperties.yml");
        fileExists(serverProperties);
        propertiesLoader = YamlConfigurationLoader.builder().file(serverProperties).build();
        try {
            propertiesConfigurationNode = propertiesLoader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }

        createDefaultsServerProperties(propertiesConfigurationNode);
        try {
            propertiesLoader.save(propertiesConfigurationNode);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
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
        createDefaultsPermissionLoader(permissionsNode);
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
        createDefaultsWhitelist(whitelistListNode);
        try {
            whitelistLoader.save(whitelistListNode);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
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

    public ConfigurationNode createPlayersData(String player) {
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
            createDefaultsPlayerData(c,temp,playerData.getName(),player);
            return c;
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
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
        ConfigurationNode permissions = wanted.node("Permissions");
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
    public ConfigurationNode getPropertiesConfigurationNode(){
        return propertiesConfigurationNode;
    }

    private void createDefaultsServerProperties(ConfigurationNode serverProp){
        try {
            if (serverProp.node("server","port").isNull()){
                serverProp.node("server","port").set(25565);
            }
            if (serverProp.node("server","host").isNull()){
                serverProp.node("server","host").set("localHost");
            }
            if (serverProp.node("velocity","active").isNull()){
                serverProp.node("velocity","active").set(false);
            }
            if (serverProp.node("velocity","secret").isNull()){
                serverProp.node("velocity","secret").set("null");
            }
        } catch (SerializationException e) {
            System.err.println("Properties defaults setting error "+e.getMessage());
        }
    }
    private void createDefaultsWhitelist(ConfigurationNode whitelist){
        try {
            if (whitelist.node("whitelist","active").isNull()){
                whitelist.node("whitelist","active").set(false);
            }
            if (whitelist.node("whitelist","players").isNull()){
                whitelist.node("whitelist","players").set(new ArrayList<>());
            }
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
    private void createDefaultsPlayerData(ConfigurationNode playerData, YamlConfigurationLoader playerDataLoader, String username, String uuid){
        try {
            String time = getTime();
            if (playerData.node("Player_Info","Identifiers","UUID").isNull()){
                playerData.node("Player_Info","Identifiers","UUID").set(username);
            }
            if (playerData.node("Player_Info","Identifiers","Username").isNull()){
                playerData.node("Player_Info","Identifiers","Username").set(uuid);
            }
            if (playerData.node("Player_Info","Activity","LastOnline").isNull()){
                playerData.node("Player_Info","Activity","LastOnline").set(time);
            }
            if (playerData.node("Player_Info","Activity","FirstJoined").isNull()){
                playerData.node("Player_Info","Activity","FirstJoined").set(time);
            }
            if (playerData.node("Permissions").isNull()){
                playerData.node("Permissions").set("");
            }
            if (playerData.node("Cosmetics").isNull()){
                playerData.node("Cosmetics").set("");
            }
            if (playerData.node("Achievements").isNull()){
                playerData.node("Achievements").set("");
            }
            playerDataLoader.save(playerData);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDefaultsPermissionLoader(ConfigurationNode permissionsNode){
        try {
            if (permissionsNode.node("permissions").isNull()){
                permissionsNode.node("permissions").set(null);
            }
            permissionsLoader.save(permissionsNode);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }
}
