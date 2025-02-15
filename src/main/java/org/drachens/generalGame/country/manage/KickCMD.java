package org.drachens.generalGame.country.manage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.CountryLeaveEvent;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class KickCMD extends Command {
    public KickCMD() {
        super("accept");
        setCondition((sender, s) -> isLeaderOfCountry(sender));
        var players = ArgumentType.String("Username")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!isLeaderOfCountry(sender)) return;
                    CPlayer player = (CPlayer) sender;
                    List<String> playerNames = new ArrayList<>();
                    player.getCountry().getPlayers().forEach(p -> playerNames.add(p.getUsername()));
                    getSuggestionBasedOnInput(suggestion, playerNames);
                });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            String player = context.get(players);
            CPlayer senders = (CPlayer) sender;
            CPlayer p = (CPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(player);
            if (p == null) return;
            if (p.getCountry() == null || p.getCountry() != senders.getCountry()) return;
            Country country = p.getCountry();
            EventDispatcher.call(new CountryLeaveEvent(country, p));
            country.removePlayer(p);
        }, players);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
