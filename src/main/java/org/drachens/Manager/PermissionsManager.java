package org.drachens.Manager;

import net.minestom.server.permission.Permission;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionsManager {
    public PermissionsManager() {
        System.out.println("Loading permission groups...");
        ConfigurationNode perms = ContinentalManagers.configFileManager.getPermissionsFile();
        if (perms == null) {
            System.err.println("Perms was null!");
            return;
        }
        ConfigurationNode perm = perms.node("permissions");
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : perm.childrenMap().entrySet()) {
            ConfigurationNode group = entry.getValue();
            System.out.println("Loading group " + entry.getKey().toString());
            List<Permission> groupsPermissions = new ArrayList<>();
            for (Map.Entry<Object, ? extends ConfigurationNode> entry2 : group.childrenMap().entrySet()) {
                groupsPermissions.add(new Permission(entry2.getKey().toString()));
            }
            ContinentalManagers.permissions.addPermissionGroup(entry.getKey().toString(), groupsPermissions, false);
        }
    }

}
