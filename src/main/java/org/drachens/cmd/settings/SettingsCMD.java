package org.drachens.cmd.settings;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.settings.premium.autovote.AutoVoteCMD;
import org.drachens.cmd.settings.premium.login.LoginMessageCMD;
import org.drachens.cmd.vote.VotingOptionCMD;

import java.util.List;

public class SettingsCMD extends Command {
    public SettingsCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("settings");

        addSubcommand(new LoginMessageCMD());
        addSubcommand(new AutoVoteCMD(votingOptionsCMD));
    }
}
