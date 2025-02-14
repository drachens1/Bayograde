package org.drachens.fileManagement.customTypes;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.drachens.fileManagement.filetypes.GsonFileType;
import java.util.HashSet;

public class WhitelistFile extends GsonFileType {
    private final HashSet<String> players = new HashSet<>();
    public WhitelistFile() {
        super("whitelist.yml");
        setDefaults();
        initialLoad();
    }

    @Override
    protected void initialLoad() {
        players.addAll(getFromList(String.class,"whitelist","players"));
    }

    @Override
    protected void setDefaults() {
        addDefault(new JsonPrimitive(false),"whitelist","active");
        addDefault(new JsonArray(),"whitelist","players");
    }

    public boolean isActive(){
        return getConfig().getAsJsonObject("whitelist").get("active").getAsBoolean();
    }

    public void toggle(boolean b){
        set(new JsonPrimitive(b),"whitelist","active");
        saveToFile();
    }

    public HashSet<String> getPlayers(){
        return players;
    }

    public boolean whiteListContains(String player){
        return players.contains(player);
    }

    public void addPlayer(String player){
        addToList(player,String.class,"whitelist","players");
        players.add(player);
        saveToFile();
    }

    public void removePlayer(String player){
        removeFromList(player,String.class,"whitelist","players");
        players.add(player);
        saveToFile();
    }
}
