package org.drachens.Manager.defaults;

import dev.ng5m.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionsUtil {
    private final List<String> operator = new ArrayList<>();
    private final HashMap<String, List<String>> groups = new HashMap<>();

    public PermissionsUtil() {
        operator.add("ban");
        operator.add("unban");
        operator.add("whitelist");
        operator.add("operator");
        operator.add("kill");
        operator.add("editPermissions");
        operator.add("gamemode");
        operator.add("debug");
        operator.add("reset");
        operator.add("stop");
        operator.add("restart");
        operator.add("summon");
        operator.add("cheat");//For gold permission
    }

    public void playerOp(CPlayer p) {
        System.out.println(p.getUsername() + " was opped");
        operator.forEach(p::addPermission);
        p.addPermission("operator");
    }
}
