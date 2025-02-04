package org.drachens.fileManagement.customTypes;

import org.drachens.fileManagement.filetypes.YamlFileType;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashSet;

public class WhitelistFile extends YamlFileType {
    private final HashSet<String> players = new HashSet<>();
    public WhitelistFile() {
        super("whitelist", "whitelist.yml");
        setDefaults();
        initialLoad();
    }

    @Override
    protected void initialLoad() {
        players.addAll(getFromList(String.class,"whitelist","players"));
    }

    @Override
    protected void setDefaults() {
        try {
            addDefault(false,"whitelist","active");
            addDefault(new ArrayList<>(),"whitelist","players");
            save();
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isActive(){
        return getConfigurationNode().node("whitelist","active").getBoolean();
    }

    public void toggle(boolean b){
        set(b,"whitelist","active");
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
        save();
    }

    public void removePlayer(String player){
        removeFromList(player,String.class,"whitelist","players");
        players.add(player);
        save();
    }
}
