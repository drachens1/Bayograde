package org.drachens.dataClasses.other;

import dev.ng5m.CPlayer;
import net.minestom.server.utils.PacketUtils;

import java.util.List;
public class VisbilityHandler {
    public void updateEntityVisibility(CPlayer player) {
        List<Clientside> clientsides = player.getClientsides();
        for (Clientside clientside : clientsides) {
            double distance = player.getPosition().distance(clientside.pos);
            if (distance <= 5) {
                showEntityToPlayer(player, clientside);
            } else {
                hideEntityFromPlayer(player, clientside);
            }
        }
    }

    private void showEntityToPlayer(CPlayer player, Clientside entity) {
        if (!entity.isVisible(player)){
            entity.addVisible(player);
            PacketUtils.sendPacket(player,entity.getSpawnPacket());
        }

    }

    private void hideEntityFromPlayer(CPlayer player, Clientside entity) {
        entity.removeVisible(player);
        PacketUtils.sendPacket(player,entity.getDestroyPacket());
    }
}
