package org.drachens.temporary.country;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;


public class AllInformationCMD extends Command {
    public AllInformationCMD() {
        super("all_information");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            getSuggestionBasedOnInput(suggestion, context.getInput(), 2, getCountryNames(p.getInstance()));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            List<Component> modifierComps = new ArrayList<>();
            for (Modifier modifier : country.getModifiers()) {
                modifierComps.add(modifier.getName());
                modifierComps.add(Component.text(", "));
            }
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text(getName(), NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Country: "))
                    .append(country.getNameComponent())
                    .appendNewline()
                    .append(Component.text("Leader: "))
                    .append(Component.text()
                            .append(country.getLeader().getName())
                            .clickEvent(ClickEvent.runCommand("/country leader " + getName()))
                            .hoverEvent(HoverEvent.showText(country.getLeader().getDescription()))
                    )
                    .appendNewline()
                    .append(Component.text("Region: "))
                    .append(Component.text(country.getRegion().getFirst().getName()))
                    .append(Component.text(", "))
                    .append(Component.text(country.getRegion().getLast().getName()))
                    .appendNewline()
                    .append(Component.text("Elections type: "))
                    .append(country.getElections().getCurrentElectionType().getName())
                    .appendNewline()
                    .append(Component.text("Ideology type: "))
                    .append(country.getIdeology().getCurrentIdeology().getName())
                    .appendNewline()
                    .append(Component.text("Types:"))
                    .append(Component.text(" ,"))
                    .append(Component.text(country.getRelationsStyle().name()))
                    .append(Component.text(" ,"))
                    .append(Component.text(country.getHistory().name()))
                    .append(Component.text(" ,"))
                    .append(Component.text(country.getFocuses().name()))
                    .append(Component.text(" ,"))
                    .append(Component.text(country.getPreviousWar().name()))
                    .append(Component.text(" ,"))
                    .appendNewline()
                    .append(Component.text("Power: "))
                    .append(Component.text(country.getType().name()))
                    .appendNewline()
                    .append(Component.text("Modifiers: "))
                    .append(modifierComps)
                    .build());
        }, countries);
    }
}
