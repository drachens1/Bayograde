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
}
