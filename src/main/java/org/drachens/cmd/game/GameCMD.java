package org.drachens.cmd.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.drachens.Manager.ServerHealthManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.generalGame.country.info.Info;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.OtherUtil.formatPlaytime;

public class GameCMD extends Command {
    public GameCMD() {
        super("server");
        addSubcommand(new Info());

        addSubcommand(new CommandCreator("leave")
                .setCondition((sender, s)->{
                    CPlayer p = (CPlayer) sender;
                    return p.isInOwnGame();
                })
                .setDefaultExecutor((sender, context)->{
                    CPlayer p = (CPlayer) sender;
                    if (!p.isInOwnGame()) return;
                    if (p.getInstance() != ContinentalManagers.worldManager.getDefaultWorld().getInstance()) {
                        p.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
                    }
                    p.teleport(new Pos(0, 1, 0));
                })
                .build());

        addSubcommand(new CommandCreator("list")
                .setDefaultExecutor((sender, context) -> {
                    StringBuilder players = new StringBuilder();
                    players.append("Player list:").append('\n');
                    for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        players.append(p.getUsername()).append(',');
                    }
                    players.setCharAt(players.lastIndexOf(","), ' ');
                    sender.sendMessage(players.toString());
                })
                .build());

        addSubcommand(new CommandCreator("ping")
                .setDefaultExecutor((sender, context) -> {
                    if (!(sender instanceof CPlayer p)) return;
                    p.sendMessage(Component.text()
                            .append(Component.text(p.getLatency(), NamedTextColor.GREEN))
                            .append(Component.text(" MS", NamedTextColor.GREEN)));
                })
                .build());

        addSubcommand(new CommandCreator("tps")
                .setDefaultExecutor((sender, context) -> {
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
                            .append(Component.text(null == ten ? "No data" : serverHealthManager.getTpsFromSecondsAgo(10).toString()))
                            .appendNewline()
                            .append(Component.text("TPS 1m ago: "))
                            .append(Component.text(null == sixty ? "No data" : sixty.toString()))
                            .appendNewline()
                            .append(Component.text("TPS 5m ago: "))
                            .append(Component.text(null == five ? "No data" : five.toString()))
                            .appendNewline()
                            .build());
                })
                .build());
    }
}
