package org.drachens.temporary.country.diplomacy.justifywar;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalTypeEnum;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class JustifyAgainstCMD extends Command {
    public JustifyAgainstCMD() {
        super("against");
        var countries = getCountriesArgExcludingPlayersCountry();

        List<String> opts = Arrays.stream(new String[]{WarGoalTypeEnum.justified.name(),WarGoalTypeEnum.partially_justified.name(),WarGoalTypeEnum.surprise.name()}).toList();

        var option = ArgumentType.String("Option")
                .setSuggestionCallback((sender,context,suggestion)-> getSuggestionBasedOnInput(suggestion,opts));

        HashSet<String> stuff = new HashSet<>(opts);

        addSyntax((sender,context)-> sender.sendMessage("Proper usage /country diplomacy justify_war against <country> <type>"),option);

        addSyntax((sender,context)->{
            if (!isLeaderOfCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country against = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country.isFriend(against)){
                p.sendMessage("You cant justify on yourself/ally/puppet/non-aggression pact");
                return;
            }
            String choice = context.get(option);
            if (!stuff.contains(choice)){
                p.sendMessage("That is not a valid option");
                return;
            }
            WarGoalTypeEnum warGoalTypeEnum = WarGoalTypeEnum.valueOf(choice);
            WarJustification warJustification = new WarJustification(warGoalTypeEnum.getWarGoalType(),against);
            EventDispatcher.call(new WarJustificationStartEvent(warJustification,country));
        },option,countries);
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
