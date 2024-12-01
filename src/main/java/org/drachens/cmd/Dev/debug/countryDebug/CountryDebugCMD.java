package org.drachens.cmd.Dev.debug.countryDebug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.KyoriUtil.mergeComp;
import static org.drachens.util.ServerUtil.getWorldClasses;

public class CountryDebugCMD extends Command {
    public CountryDebugCMD(String permission) {
        super("debug");
        setCondition((sender, permissionName) -> sender.hasPermission(permission));
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player p)) return;
            List<Component> components = new ArrayList<>();
            components.add(compBuild("Countries: ", NamedTextColor.BLUE));
            switch (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner){
                case VotingWinner.none -> {
                    return;
                }
                case VotingWinner.ww2_clicks -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        ClicksCountry country = (ClicksCountry) c;
                        Component countryComp = Component.text()
                                .append(Component.text(country.getType().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getHistory().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(country.getIdeology().getCurrentIdeology().getName())
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getElections() + "", NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getFocuses().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getPreviousWar().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getRelationsStyle().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(country.getNameComponent())
                                .appendNewline()
                                .build();
                        components.add(countryComp);
                    }
                }
                case VotingWinner.ww2_troops -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        TroopCountry country = (TroopCountry) c;
                        Component countryComp = Component.text()
                                .append(Component.text(country.getType().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getHistory().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(country.getIdeology().getCurrentIdeology().getName())
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getElections() + "", NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getFocuses().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getPreviousWar().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(Component.text(country.getRelationsStyle().name(), NamedTextColor.GOLD))
                                .append(Component.text(",", NamedTextColor.BLUE))
                                .append(country.getNameComponent())
                                .appendNewline()
                                .build();
                        components.add(countryComp);
                    }
                }
            }
            p.sendMessage(mergeComp(components));
        });
    }
}
