package org.drachens.temporary.country.info;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.VotingWinner;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;


public class AllInformationCMD extends Command {
    public AllInformationCMD() {
        super("all");
        var countries = getCountriesArg();

        setDefaultExecutor((sender,context)->{
            if (!(sender instanceof CPlayer p))
                return;
            Country country = p.getCountry();
            if (country == null){
                p.sendMessage("Join a country or do /country info all");
                return;
            }
            run(country,p);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country c = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (c == null){
                p.sendMessage("That is not a valid country");
                return;
            }

            run(c,p);
        }, countries);
    }

    public void run(Country c, CPlayer p){
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : c.getModifiers()) {
            modifierComps.add(modifier.getName());
            modifierComps.add(Component.text(", "));
        }
        switch (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner) {
            case VotingWinner.ww2_clicks -> {
                ClicksCountry country = (ClicksCountry) c;
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
            }
            case VotingWinner.ww2_troops -> {
                TroopCountry country = (TroopCountry) c;
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
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner);
        }
    }
}
