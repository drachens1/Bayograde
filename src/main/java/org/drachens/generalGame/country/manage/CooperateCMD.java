package org.drachens.generalGame.country.manage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.CountryCoopPlayerEvent;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class CooperateCMD extends Command {
    public CooperateCMD() {
        super("co-op");
        setCondition((sender, s) -> isLeaderOfCountry(sender));
        var players = ArgumentType.String("Username")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!isLeaderOfCountry(sender)) return;
                    CPlayer player = (CPlayer) sender;
                    List<String> playerNames = new ArrayList<>();
                    player.getInstance().getPlayers().forEach(p -> playerNames.add(p.getUsername()));
                    player.getCountry().getPlayers().forEach(p-> playerNames.remove(p.getUsername()));
                    getSuggestionBasedOnInput(suggestion, playerNames);
                });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer senders = (CPlayer) sender;
            CPlayer p = (CPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(players));
            if (p == null) return;
            if (p.getCountry() != null) return;
            EventDispatcher.call(new CountryCoopPlayerEvent(senders.getCountry(), p));
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
