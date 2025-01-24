package org.drachens.fileManagement;

import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.customTypes.WhitelistFile;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigFileManager {
    private ConfigurationNode permissionsNode;
    private YamlConfigurationLoader permissionsLoader;  // Has the permission groups that are created
    private String logMsg;
    private String logCmds;
    private ServerPropertiesFile serverPropertiesFile;
    private WhitelistFile whitelistFile;

    public void startup() {
        serverPropertiesFile = new ServerPropertiesFile();
        whitelistFile = new WhitelistFile();

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

    public WhitelistFile getWhitelistFile() {
        return whitelistFile;
    }

    public void specificSave(String choice) {
        System.out.println("Specifically saving: " + choice);
        switch (choice) {
            case "permissions":
                try {
                    permissionsLoader.save(permissionsNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving permissions: " + e.getMessage());
                }
        }
    }

    public ServerPropertiesFile getServerPropertiesFile() {
        return serverPropertiesFile;
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
