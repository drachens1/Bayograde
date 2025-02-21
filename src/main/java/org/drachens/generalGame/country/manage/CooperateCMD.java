package org.drachens.generalGame.country.manage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.events.countries.CountryCoopPlayerEvent;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class CooperateCMD extends Command {
    public CooperateCMD() {
        super("co-op");
        Argument<String> players = ArgumentType.String("Username")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer player = (CPlayer) sender;
                    List<String> playerNames = new ArrayList<>();
                    player.getInstance().getPlayers().forEach(p -> playerNames.add(p.getUsername()));
                    player.getCountry().getInfo().getPlayers().forEach(p-> playerNames.remove(p.getUsername()));
                    getSuggestionBasedOnInput(suggestion, playerNames);
                });

        addSyntax((sender, context) -> {
            CPlayer senders = (CPlayer) sender;
            CPlayer p = (CPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(players));
            if (null == p) return;
            if (null != p.getCountry()) return;
            EventDispatcher.call(new CountryCoopPlayerEvent(senders.getCountry(), p));
        }, players);
    }
}
