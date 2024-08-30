package org.drachens.cmd;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ListCMD extends Command {
    public ListCMD() {
        super("list");
        setDefaultExecutor((sender,context)->{
            StringBuilder players = new StringBuilder();
            players.append("Player list:").append("\n");
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
                players.append(p.getUsername()).append(",");
            }
            players.setCharAt(players.lastIndexOf(","),' ');
            sender.sendMessage(players.toString());
        });
    }
}
