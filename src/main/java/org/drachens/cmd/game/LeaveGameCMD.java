package org.drachens.cmd.game;

import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class LeaveGameCMD extends Command {
    public LeaveGameCMD() {
        super("leave");

        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isInOwnGame()||p.isInInterchange();
        });

        setDefaultExecutor((sender,context)->{
            CPlayer p = (CPlayer) sender;
            if (!p.isInOwnGame()||!p.isInInterchange())return;
            if (p.getInstance() != ContinentalManagers.worldManager.getDefaultWorld().getInstance()) {
                p.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
            }
            p.teleport(new Pos(0, 1, 0));
        });
    }
}
