package org.drachens.Manager;

import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.AdminMessageType;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdminManager {
    private final List<CPlayer> admins = new ArrayList<>();

    public void broadcast(AdminMessageType adminMessageType, Component msg){
        admins.forEach(player -> player.sendMessage(Component.text().append(adminMessageType.prefix).append(msg)));
    }

    public void broadcast(AdminMessageType adminMessageType, Component msg, Instance instance){
        admins.forEach(player -> {
            if (player.getInstance()!=instance)return;
            player.sendMessage(Component.text().append(adminMessageType.prefix).append(msg));
        });
    }

    public void addAdmin(CPlayer p){
        admins.add(p);
    }

    public void removeAdmin(CPlayer p){
        admins.remove(p);
    }

    public List<CPlayer> getAdmins(){
        return admins;
    }
}
