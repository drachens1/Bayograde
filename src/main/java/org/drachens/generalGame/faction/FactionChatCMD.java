package org.drachens.generalGame.faction;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.dataClasses.Diplomacy.faction.FactionChat;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

public class FactionChatCMD extends Command {
    private final HashMap<CPlayer, Boolean> active = new HashMap<>();

    public FactionChatCMD() {
        super("chat");

        var factionsArg = ArgumentType.String("faction name")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (country.isInAnEconomicFaction()) {
                            suggestion.addEntry(new SuggestionEntry(country.getEconomyFactionType().getStringName()));
                        }
                        if (country.isInAMilitaryFaction()) {
                            suggestion.addEntry(new SuggestionEntry(country.getMilitaryFactionType().getStringName()));
                        }
                    }
                });

        setCondition((sender, s) -> isInAFaction(sender));

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /faction chat <faction> "));

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Faction faction = ContinentalManagers.world(p.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (faction == null || !faction.containsCountry(p.getCountry())) return;
            FactionChat factionChat = faction.getFactionChat();
            boolean current = active.getOrDefault(p, false);
            current = !current;
            active.put(p, current);
            if (current) {
                factionChat.addPlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.faction.getComponent())
                        .append(Component.text("Faction chat is now active")));
            } else {
                factionChat.removePlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.faction.getComponent())
                        .append(Component.text("Faction chat is now inactive")));
            }
        }, factionsArg);
    }

    private boolean isInAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isInAFaction();
        }
        return false;
    }
}
