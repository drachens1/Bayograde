package org.drachens.cmd.info;

import net.minestom.server.command.builder.Command;
import org.drachens.temporary.country.info.Info;

public class InfoCMD extends Command {
    public InfoCMD() {
        super("server");

        addSubcommand(new ListCMD());
        addSubcommand(new Info());
        addSubcommand(new PingCMD());
        addSubcommand(new TpsCMD());
    }
}
