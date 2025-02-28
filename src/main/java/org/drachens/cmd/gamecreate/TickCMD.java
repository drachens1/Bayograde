package org.drachens.cmd.gamecreate;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.player_types.CPlayer;

public class TickCMD extends Command {
    public TickCMD() {
        super("tick");

        addSubcommand(new CommandCreator("pause")
                .setDefaultExecutor((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    ContinentalManagers.yearManager.getYearBar(p.getInstance()).pause();
                })
                .build());

        addSubcommand(new CommandCreator("unpause")
                .setDefaultExecutor((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    ContinentalManagers.yearManager.getYearBar(p.getInstance()).unpause();
                })
                .build());
    }
}
