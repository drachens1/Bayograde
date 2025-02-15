package org.drachens.cmd.game;

import net.minestom.server.command.builder.Command;
import org.drachens.generalGame.country.info.Info;

public class GameCMD extends Command {
    public GameCMD() {
        super("server");

        addSubcommand(new ListCMD());
        addSubcommand(new Info());
        addSubcommand(new PingCMD());
        addSubcommand(new TpsCMD());
        addSubcommand(new LeaveGameCMD());
    }
}
