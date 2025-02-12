package org.drachens.cmd.settings;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.settings.premium.login.LoginMessageCMD;

public class SettingsCMD extends Command {
    public SettingsCMD() {
        super("settings");

        addSubcommand(new LoginMessageCMD());
    }
}
