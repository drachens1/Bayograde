package org.drachens.fileManagement.customTypes;

import org.drachens.fileManagement.YamlFileType;
import org.spongepowered.configurate.serialize.SerializationException;

public class ServerPropertiesFile extends YamlFileType {
    public ServerPropertiesFile() {
        super("serverProperties", "serverProperties.yml");
        setDefaults();
        initialLoad();
    }

    @Override
    protected void initialLoad() {

    }

    @Override
    protected void setDefaults() {
        try {
            addDefault(25565, "server", "port");
            addDefault("localhost", "server", "host");
            addDefault(false, "velocity", "active");
            addDefault("null", "velocity", "secret");
            addDefault("", "database", "host");
            addDefault(25560, "database", "port");
            addDefault("", "database", "user");
            addDefault("", "database", "password");
            save();
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return getConfigurationNode().node("server", "port").getInt();
    }

    public String getHost() {
        return getConfigurationNode().node("server", "host").getString();
    }

    public boolean isVelocity() {
        return getConfigurationNode().node("velocity", "active").getBoolean();
    }

    public String getSecret() {
        return getConfigurationNode().node("velocity", "secret").getString();
    }

    public String getDatabaseHost() {
        return getConfigurationNode().node("database", "host").getString();
    }

    public String getDatabaseUser() {
        return getConfigurationNode().node("database", "user").getString();
    }

    public String getDatabasePassword() {
        return getConfigurationNode().node("database", "password").getString();
    }

    public int getDatabasePort() {
        return getConfigurationNode().node("database", "port").getInt();
    }
}
