package org.drachens.cmd.Dev;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Team;
import org.drachens.InventorySystem.GUIManager;

public class testCMD extends Command {
    public testCMD(GUIManager guiManager) {
        super("test");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            guiManager.openGUI(new testInv(), p);
            for (Team team : MinecraftServer.getTeamManager().getTeams()) {
                sender.sendMessage(team.getTeamName());
            }
        });

    }
}
