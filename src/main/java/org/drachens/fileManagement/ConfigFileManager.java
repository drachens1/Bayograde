package org.drachens.fileManagement;

import net.minestom.server.entity.Player;
import org.drachens.cmd.Dev.whitelist.Whitelist;
import org.drachens.fileManagement.customTypes.PlayerDataFile;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConfigFileManager {
    private ConfigurationNode whitelistListNode;
    private YamlConfigurationLoader whitelistLoader;    // contains all the whitelisted peoples as UUID's
    private ConfigurationNode permissionsNode;
    private YamlConfigurationLoader permissionsLoader;  // Has the permission groups that are created
    private String logMsg;
    private String logCmds;
    private Whitelist whitelist;
    private ServerPropertiesFile serverPropertiesFile;

    public void startup() {
        File playerData = new File("playerData");//Creates the parent directory
        playerData.mkdir();

        serverPropertiesFile = new ServerPropertiesFile();

        //To setup all the log stuff
        System.out.println("Creating logs");
        new File("logs").mkdir();
        new File("logs/cmds").mkdir();
        new File("logs/msg").mkdir();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy.HH.mm");
        logCmds = "logs/cmds/" + format.format(new Date()) + ".txt";
        File cmd = new File(logCmds);
        fileExists(cmd);
        logMsg = "logs/msg/" + format.format(new Date()) + ".txt";
        File msg = new File(logMsg);
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
    }

    private void loadWhitelist() {
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

    public void createPlayersData(Player player) {
        new PlayerDataFile(player);
    }

    private void fileExists(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                System.out.println(f.getName() + " was created");
            } catch (IOException e) {
                //don't need anything here
            }
        }
    }

    public String getLogMsg() {
        return logMsg;
    }

    public String getLogCmds() {
        return logCmds;
    }

    public Whitelist getWhitelist() {
        return whitelist;
    }

    public ConfigurationNode getWhitelistListNode() {
        return whitelistListNode;
    }

    public void specificSave(String choice) {
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

    public ConfigurationNode getPermissionsFile() {
        return permissionsNode;
    }

    public ServerPropertiesFile getServerPropertiesFile() {
        return serverPropertiesFile;
    }

    private void createDefaultsServerProperties(ConfigurationNode serverProp) {
        try {
            if (serverProp.node("server", "port").isNull()) {
                serverProp.node("server", "port").set(25565);
            }
            if (serverProp.node("server", "host").isNull()) {
                serverProp.node("server", "host").set("localHost");
            }
            if (serverProp.node("velocity", "active").isNull()) {
                serverProp.node("velocity", "active").set(false);
            }
            if (serverProp.node("velocity", "secret").isNull()) {
                serverProp.node("velocity", "secret").set("null");
            }
        } catch (SerializationException e) {
            System.err.println("Properties defaults setting error " + e.getMessage());
        }
    }

    private void createDefaultsWhitelist(ConfigurationNode whitelist) {
        try {
            if (whitelist.node("whitelist", "active").isNull()) {
                whitelist.node("whitelist", "active").set(false);
            }
            if (whitelist.node("whitelist", "players").isNull()) {
                whitelist.node("whitelist", "players").set(new ArrayList<>());
            }
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDefault(ConfigurationNode c, Object data, Object... path) throws SerializationException {
        if (c.node(path).isNull()) {
            c.node(path).set(data);
        }
    }

    private void createDefaultsPermissionLoader(ConfigurationNode permissionsNode) {
        try {
            if (permissionsNode.node("permissions").isNull()) {
                permissionsNode.node("permissions").set(null);
            }
            permissionsLoader.save(permissionsNode);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }
}
