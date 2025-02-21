package org.drachens.generalGame.country.manage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.events.countries.CountrySetLeaderEvent;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class SetLeaderCMD extends Command {

    public SetLeaderCMD() {
        super("set_leader");
        var players = ArgumentType.String("Username")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer player = (CPlayer) sender;
                    List<String> playerNames = new ArrayList<>();
                    player.getCountry().getPlayers().forEach(p -> playerNames.add(p.getUsername()));
                    getSuggestionBasedOnInput(suggestion, playerNames);
                });

        addSyntax((sender, context) -> {
            String player = context.get(players);
            CPlayer senders = (CPlayer) sender;
            CPlayer p = (CPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(player);
            if (p == null) return;
            if (p.getCountry() == null || p.getCountry() != senders.getCountry()) return;
            Country country = p.getCountry();
            EventDispatcher.call(new CountrySetLeaderEvent(country, p));
        }, players);
    }
}
