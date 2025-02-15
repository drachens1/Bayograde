package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class TestCmd extends Command {
    public TestCmd() {
        super("test");

        setDefaultExecutor((sender,context)->{
            CPlayer p = (CPlayer) sender;
            System.out.println(p.getCountry().toJson());
        });
    }
}
