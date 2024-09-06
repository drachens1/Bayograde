package org.drachens.Manager;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.drachens.Manager.ConfigFileManager.*;
import static org.drachens.util.Messages.globalBroadcast;

public class WhitelistManager {
    public void addPlayer(UUID p){
        //Handles the configuration part so it is persistent
        ConfigurationNode whitelistNode = getWhitelistListNode();
        ConfigurationNode whitelist = whitelistNode.node("whitelist");
        ConfigurationNode player = whitelist.node("players");
        try {
            List<String> uuids = player.getList(String.class);
            if (uuids == null) {
                uuids = new ArrayList<>();
            }
            uuids.add(p.toString());
            player.setList(String.class, uuids);
            specificSave("whitelist");
        } catch (SerializationException e) {
            System.out.println("Add player error "+e.getMessage());
        }
        //Handles the temporary part
        getWhitelist().addPlayer(p);
    }

    public void removePlayer(UUID p){
        //Handles the configuration part so it is persistent
        ConfigurationNode whitelistNode = getWhitelistListNode();
        ConfigurationNode whitelist = whitelistNode.node("whitelist");
        ConfigurationNode player = whitelist.node("players");
        try {
            List<String> uuids = player.getList(String.class);
            if (uuids == null) {
                uuids = new ArrayList<>();
            }
            uuids.remove(p.toString());
            player.setList(String.class, uuids);
            specificSave("whitelist");
        } catch (SerializationException e) {
            System.out.println("Add player error "+e.getMessage());
        }
        //Handles the temporary part
        getWhitelist().removePlayer(p);
    }

    public void toggle(boolean e){
        globalBroadcast(e+"");
        //Handles the configuration part so it is persistent
        ConfigurationNode whitelistNode = getWhitelistListNode();
        ConfigurationNode whitelist = whitelistNode.node("whitelist");
        ConfigurationNode actives = whitelist.node("active");
        try {
            actives.set(e);
            specificSave("whitelist");
        } catch (SerializationException ex) {
            System.out.println("Toggle error "+ex.getMessage());
        }


        //Handles the temporary part
        getWhitelist().setActive(e);
    }
}
