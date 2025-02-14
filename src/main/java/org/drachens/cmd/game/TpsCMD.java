package org.drachens.cmd.game;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.ServerHealthManager;
import org.drachens.Manager.defaults.ContinentalManagers;

import static org.drachens.util.OtherUtil.formatPlaytime;

public class TpsCMD extends Command {
    public TpsCMD() {
        super("tps");

        setDefaultExecutor((sender, context) -> {
            ServerHealthManager serverHealthManager = ContinentalManagers.serverHealthManager;

            Double ten = serverHealthManager.getTpsFromSecondsAgo(10);
            Double sixty = serverHealthManager.getTpsFromSecondsAgo(60);
            Double five = serverHealthManager.getTpsFromSecondsAgo(300);

            sender.sendMessage(Component.text()
                    .appendNewline()
                    .append(Component.text("Server uptime: "))
                    .append(Component.text(formatPlaytime(serverHealthManager.getUptime())))
                    .appendNewline()
                    .append(Component.text("Current TPS: "))
                    .append(Component.text(serverHealthManager.getTps()))
                    .appendNewline()
                    .append(Component.text("TPS 10s ago: "))
                    .append(Component.text(ten == null ? "No data" : serverHealthManager.getTpsFromSecondsAgo(10).toString()))
                    .appendNewline()
                    .append(Component.text("TPS 1m ago: "))
                    .append(Component.text(sixty == null ? "No data" : sixty.toString()))
                    .appendNewline()
                    .append(Component.text("TPS 5m ago: "))
                    .append(Component.text(five == null ? "No data" : five.toString()))
                    .appendNewline()
                    .build());
        });
    }
}
