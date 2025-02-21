package org.drachens.generalGame.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.events.factions.FactionJoinEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getFactionNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction join <faction>"));
        setCondition((sender, s) -> notInAFaction(sender));

        Argument<String> factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof Player player) {
                        getSuggestionBasedOnInput(suggestion, getFactionNames(player.getInstance()));
                    }
                });


        addConditionalSyntax((sender, s) -> notInAFaction(sender), (sender, context) -> {
            CPlayer player = (CPlayer) sender;
            Country country = player.getCountry();

            if (country.getDiplomacy().hasCondition(ConditionEnum.cant_join_faction)) {
                player.sendMessage(Component.text("You have the cant join a faction condition.", NamedTextColor.RED));
                return;
            }

            Faction faction = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factions));
            if (null == faction) {
                player.sendMessage("Cannot find that faction.");
                return;
            }

            EventDispatcher.call(new FactionJoinEvent(faction, country));
        }, factions);
    }

    private boolean notInAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return null != country && country.isInAllFactions();
        }
        return false;
    }
}
