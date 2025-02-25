package org.drachens.generalGame.country.diplomacy.justifywar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalType;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalTypeEnum;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;
import org.drachens.player_types.CPlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class JustifyAgainstCMD extends Command {
    public JustifyAgainstCMD() {
        super("against");
        Argument<String> countries = getCountriesArgExcludingPlayersCountry();

        List<String> opts = Arrays.stream(new String[]{WarGoalTypeEnum.justified.name(), WarGoalTypeEnum.partially_justified.name(), WarGoalTypeEnum.surprise.name()}).toList();

        Argument<String> option = ArgumentType.String("Option")
                .setSuggestionCallback((sender, context, suggestion) -> getSuggestionBasedOnInput(suggestion, opts));

        HashSet<String> stuff = new HashSet<>(opts);

        addSyntax((sender, context) -> sender.sendMessage("Proper usage /country diplomacy justify-war against <country> <type>"), option);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country against = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country.cantStartAWarWith(against)) {
                p.sendMessage(Component.text("You cant justify on yourself/ally/puppet/non-aggression pact", NamedTextColor.RED));
                return;
            }
            if (country.getDiplomacy().hasCondition(ConditionEnum.cant_start_a_war)) {
                p.sendMessage(Component.text("You have the cant start a war condition. Therefore you cant start a war.", NamedTextColor.RED));
                return;
            }
            String choice = context.get(option);
            if (!stuff.contains(choice)) {
                p.sendMessage(Component.text("That is not a valid option", NamedTextColor.RED));
                return;
            }
            WarGoalTypeEnum warGoalTypeEnum = WarGoalTypeEnum.valueOf(choice);
            WarGoalType w = warGoalTypeEnum.getWarGoalType();
            WarJustification warJustification = new WarJustification(country,against,w.modifier(),w.timeToMake(),w.expires(),false, against.getInstance());
            EventDispatcher.call(new WarJustificationStartEvent(warJustification, country));
        }, option, countries);
    }
}
