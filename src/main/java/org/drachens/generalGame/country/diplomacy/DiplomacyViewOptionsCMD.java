package org.drachens.generalGame.country.diplomacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class DiplomacyViewOptionsCMD extends Command {
    public DiplomacyViewOptionsCMD() {
        super("view_options");
        Argument<String> countries = getCountriesArgExcludingPlayersCountry();

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country against = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == against) {
                p.sendMessage("That is not a valid country");
                return;
            }
            List<Component> comps = new ArrayList<>();
            comps.add(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("VIEW OPTIONS", NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Country: "))
                    .append(against.getComponentName())
                    .appendNewline()
                    .appendNewline()
                    .build());
            if (!country.getDiplomacy().hasCondition(ConditionEnum.cant_start_a_war) && country.canFight(against)) {
                if (null != country.getDiplomacy().getCompletedWarJustification(against.getName())) {
                    WarJustification warJustification = country.getDiplomacy().getCompletedWarJustification(against.getName());
                    comps.add(Component.text()
                            .append(Component.text(" [DECLARE WAR] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .append(Component.text("expires " + warJustification.expires(), NamedTextColor.GRAY, TextDecoration.ITALIC))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to declare war", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy declare-war " + against.getName()))
                            .build());
                } else if (null != country.getDiplomacy().getWarJustification(against.getName())) {
                    WarJustification warJustification = country.getDiplomacy().getWarJustification(against.getName());
                    comps.add(Component.text()
                            .append(Component.text(" [Justification time : ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .append(Component.text(warJustification.timeLeft()))
                            .append(Component.text("] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to cancel the justification", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy justify-war cancel " + against.getName()))
                            .build());
                } else {
                    comps.add(Component.text()
                            .append(Component.text(" [JUSTIFY WAR] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to view the justification options", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy justify-war options " + against.getName()))
                            .build());
                }
                comps.add(Component.newline());
                comps.add(Component.text()
                        .append(Component.text(" [NON-AGGRESSION-PACT] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to send this player a non aggression pact offer", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.suggestCommand("/country diplomacy non-aggression-pact create " + against.getName()+ ' '))
                        .build());
                comps.add(Component.newline());
            }
            if (!ContinentalManagers.demandManager.isPlayerActive(country)) {
                comps.add(Component.text()
                        .append(Component.text(" [DEMAND] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy demand start " + against.getName()))
                        .build());
            } else if (ContinentalManagers.demandManager.getDemand(country).getToCountry() == against) {
                comps.add(Component.text()
                        .append(Component.text(" [COMPLETE DEMAND] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy demand complete "))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to prompt you to the loan creation command", NamedTextColor.GRAY)))
                        .build());
            }
            if (country.isAtWar(against.getName())) {
                comps.add(Component.text()
                        .append(Component.text(" [PEACE DEAl] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .clickEvent(ClickEvent.runCommand("/country diplomacy peace_deal " + against.getName()))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to start a peace deal process with this player", NamedTextColor.GRAY)))
                        .build());
            }
            comps.add(Component.text()
                    .append(Component.text(" [LOAN] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to prompt you to the loan creation command", NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.suggestCommand("/country loan create " + against.getName()))
                    .build());

            p.sendMessage(Component.text().append(comps));
        }, countries);
    }
}
