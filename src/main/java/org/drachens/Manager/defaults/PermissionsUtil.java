package org.drachens.Manager.defaults;

import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.YamlUtil.addToList;

public class PermissionsUtil {
    private final List<Permission> operator = new ArrayList<>();
    private final HashMap<String, List<Permission>> groups = new HashMap<>();

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
        operator.add(new Permission("summon"));
        operator.add(new Permission("cheat"));//For gold permission
    }

    public void playerOp(Player p) {
        System.out.println(p.getUsername() + " was opped");
        operator.forEach(p::addPermission);
        p.addPermission(new Permission("operator"));
    }

    public void addPermissionGroup(String name, List<Permission> permss, boolean editFile) {
        groups.put(name, permss);
        if (!editFile) return;
        ConfigurationNode permNode = ContinentalManagers.configFileManager.getPermissionsFile();
        ConfigurationNode p = permNode.node("permissions").node(name);
        permss.forEach(perm-> addToList(p,perm.getPermissionName()));
        ContinentalManagers.configFileManager.specificSave("permissions");
    }
}
