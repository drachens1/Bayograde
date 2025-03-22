package org.drachens.fileManagement.customTypes;

import com.google.gson.JsonPrimitive;
import org.drachens.fileManagement.filetypes.GsonFileType;

public class ServerPropertiesFile extends GsonFileType {
    public ServerPropertiesFile() {
        super("serverProperties.json");
        setDefaults();
        initialLoad();
    }

    @Override
    protected void initialLoad() {

    }

    @Override
    protected void setDefaults() {
        addDefault(new JsonPrimitive(25565), "server", "port");
        addDefault("localhost", "server", "host");
        addDefault(new JsonPrimitive(false), "velocity", "active");
        addDefault("null", "velocity", "secret");
        addDefault("", "database", "host");
        addDefault(new JsonPrimitive(25560), "database", "port");
        addDefault("", "database", "user");
        addDefault("", "database", "password");
        addDefault(new JsonPrimitive(false),"resource-pack","active");
        addDefault("","resource-pack","link");
        addDefault("","resource-pack","hash");
        saveToFile();
    }

    public int getPort() {
        return getConfig().getAsJsonObject("server").get("port").getAsInt();
    }

    public String getHost() {
        return getConfig().getAsJsonObject("server").get("host").getAsString();
    }

    public boolean isVelocity() {
        return getConfig().getAsJsonObject("velocity").get("active").getAsBoolean();
    }

    public String getSecret() {
        return getConfig().getAsJsonObject("velocity").get("secret").getAsString();
    }

    public String getDatabaseHost() {
        return getConfig().getAsJsonObject("database").get("host").getAsString();
    }

    public String getDatabaseUser() {
        return getConfig().getAsJsonObject("database").get("user").getAsString();
    }

    public String getDatabasePassword() {
        return getConfig().getAsJsonObject("database").get("password").getAsString();
    }

    public int getDatabasePort() {
        return getConfig().getAsJsonObject("database").get("port").getAsInt();
    }

    public String getResourcePackLink(){
        return getConfig().getAsJsonObject("resource-pack").get("link").getAsString();
    }

    public String getResourcePackHash(){
        return getConfig().getAsJsonObject("resource-pack").get("hash").getAsString();
    }

    public boolean isActive(){
        return getConfig().getAsJsonObject("resource-pack").get("active").getAsBoolean();
    }
}
