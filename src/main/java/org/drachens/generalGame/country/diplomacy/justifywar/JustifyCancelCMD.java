package org.drachens.generalGame.country.diplomacy.justifywar;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.events.countries.warjustification.WarJustificationCancelEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class JustifyCancelCMD extends Command {
    public JustifyCancelCMD() {
        super("cancel");
        setCondition((sender, s) -> isLeaderOfCountry(sender));

        var countries = ArgumentType.String("War_justifications")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getWarJustifications());
                });

        setDefaultExecutor((sender, context) -> {
            if (isLeaderOfCountry(sender)) return;
            sender.sendMessage("Proper usage /country diplomacy justify_war <country> ");
        });
        ;

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            WarJustification warJustification = country.getCreatingWarJustificationAgainst(context.get(countries));
            if (warJustification == null) return;
            EventDispatcher.call(new WarJustificationCancelEvent(warJustification, country));
        }, countries);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p) && !country.getWarJustifications().isEmpty();
        }
        return false;
    }
}
