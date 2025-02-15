package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.Dev.ban.BanCMD;
import org.drachens.cmd.Dev.ban.UnbanCMD;
import org.drachens.cmd.Dev.gamemode.GamemodeCMD;
import org.drachens.cmd.ai.AICmd;
import org.drachens.cmd.example.ExampleCMD;
import org.drachens.player_types.CPlayer;

public class DevCMD extends Command {
    public DevCMD() {
        super("dev");

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("operator");
        });

        addSubcommand(new OperatorCMD());
        addSubcommand(new ResetCMD());
        addSubcommand(new BanCMD());
        addSubcommand(new UnbanCMD());
        addSubcommand(new GamemodeCMD());
        addSubcommand(new SummonCMD());
        addSubcommand(new ExampleCMD());
        addSubcommand(new AICmd());
        addSubcommand(new CountryBordersCMD());
        addSubcommand(new TroopSummonCMD());
        addSubcommand(new GiveRankCMD());
        addSubcommand(new TestCmd());
    }
}
