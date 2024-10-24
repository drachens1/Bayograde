package org.drachens.Manager.defaults;

import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionsUtil {
    private final List<Permission> operator = new ArrayList<>();
    private  final HashMap<String, List<Permission>> groups = new HashMap<>();

    public PermissionsUtil() {
        operator.add(new Permission("ban"));
        operator.add(new Permission("unban"));
        operator.add(new Permission("whitelist"));
        operator.add(new Permission("operator"));
        operator.add(new Permission("kill"));
        operator.add(new Permission("editPermissions"));
        operator.add(new Permission("gamemode"));
        operator.add(new Permission("debug"));
        operator.add(new Permission("reset"));
        operator.add(new Permission("stop"));
        operator.add(new Permission("restart"));
    }

    public void playerOp(Player p) {
        UUID playerID = p.getUuid();
        System.out.println(p.getUsername() + " was opped");
        operator.forEach(p::addPermission);
        ConfigurationNode playerData = ContinentalManagers.configFileManager.getPlayersData(playerID);
        ConfigurationNode permissions = playerData.node("Permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            if (!perms.contains("operator")) {
                perms.add("operator");
            }

            permissions.setList(String.class, perms);
            ContinentalManagers.configFileManager.getPlayerDataLoader(playerID).save(playerData);
        } catch (SerializationException e) {
            System.out.println("Player operator failed " + p.getUsername() + " " + e.getMessage());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public  void addPermissionGroup(String name, List<Permission> permss, boolean editFile) {
        groups.put(name, permss);
        if (!editFile) return;
        ConfigurationNode permNode = ContinentalManagers.configFileManager.getPermissionsFile();
        ConfigurationNode p = permNode.node("permissions").node(name);
        for (Permission perm : permss) {
            try {
                List<String> perms = p.getList(String.class);
                if (perms == null) {
                    perms = new ArrayList<>();
                }

                if (!perms.contains(perm.getPermissionName())) {
                    perms.add(perm.getPermissionName());
                }

                p.setList(String.class, perms);
            } catch (SerializationException e) {
                System.err.println("Error adding permission group " + e.getMessage());
            }
        }
        ContinentalManagers.configFileManager.specificSave("permissions");
    }

    public void playerAddPerm(String name, Player p) {
        for (Permission perm : groups.get(name)) {
            p.addPermission(perm);
        }
        UUID playerID = p.getUuid();
        System.out.println(p.getUsername() + " was added to group " + name);

        ConfigurationNode playerData = ContinentalManagers.configFileManager.getPlayersData(playerID);
        ConfigurationNode permissions = playerData.node("Permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            if (!perms.contains(name)) {
                perms.add(name);
            }

            permissions.setList(String.class, perms);
            ContinentalManagers.configFileManager.getPlayerDataLoader(playerID).save(playerData);
        } catch (SerializationException e) {
            System.out.println("Player add " + name + " failed " + p.getUsername() + " " + e.getMessage());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public  void playerRemovePerm(String name, Player p) {
        for (Permission perm : groups.get(name)) {
            p.removePermission(perm);
        }
        UUID playerID = p.getUuid();
        System.out.println(p.getUsername() + " was added to group " + name);

        ConfigurationNode playerData = ContinentalManagers.configFileManager.getPlayersData(playerID);
        ConfigurationNode permissions = playerData.node("Permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            perms.remove(name);

            permissions.setList(String.class, perms);
            ContinentalManagers.configFileManager.getPlayerDataLoader(playerID).save(playerData);
        } catch (SerializationException e) {
            System.out.println("Player removal " + name + " failed " + p.getUsername() + " " + e.getMessage());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }
}
