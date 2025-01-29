package org.drachens.cmd.Dev;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;

import java.util.HashSet;
import java.util.List;

public class OpMeCMD extends Command {
    private final HashSet<String> players = new HashSet<>(List.of(new String[]{"sweeville", "drachens","NG5M"}));

    public OpMeCMD() {
        super("op-me");

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (players.contains(p.getUsername())) {
                ContinentalManagers.permissions.playerOp(p);
            }
        }));
    }
}
