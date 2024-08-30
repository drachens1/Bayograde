package org.drachens.api.util;

import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.drachens.Manager.ConfigFileManager.getPlayerDataLoader;
import static org.drachens.Manager.ConfigFileManager.getPlayersData;

public class PermissionsUtil {
    private static final List<Permission> operator = new ArrayList<>();
    public static void setupPerms(){
        operator.add(new Permission("ban"));
        operator.add(new Permission("unban"));
        operator.add(new Permission("whitelist"));
        operator.add(new Permission("operator"));
    }

    public static void playerOp(Player p){
        UUID playerID = p.getUuid();
        System.out.println(p.getUsername()+" was opped");

        for (Permission perm : operator){
            p.addPermission(perm);
        }
        ConfigurationNode playerData = getPlayersData(playerID);
        ConfigurationNode permissions = playerData.node("permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            if (!perms.contains("operator")) {
                perms.add("operator");
            }

            permissions.setList(String.class, perms);
            getPlayerDataLoader(playerID).save(playerData);
        }catch (SerializationException e) {
            System.out.println("Player operator failed "+p.getUsername()+" "+e.getMessage());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }
}
