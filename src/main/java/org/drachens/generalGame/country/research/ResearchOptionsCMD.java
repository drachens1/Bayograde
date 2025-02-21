package org.drachens.generalGame.country.research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Research.ResearchCountry;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class ResearchOptionsCMD extends Command {
    public ResearchOptionsCMD() {
        super("options");

        setCondition((sender, s) -> !notCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (notCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            List<Component> comps = new ArrayList<>();
            getAvailable(country.getResearch().researchCountry(), p).forEach(string -> comps.add(Component.text()
                    .append(Component.text(string))
                    .append(Component.text()
                            .append(Component.text("[RESEARCH]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .clickEvent(ClickEvent.runCommand("/country research option " + string))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to start researching this", NamedTextColor.GOLD))))
                    .build()));
            sender.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(country.getComponentName())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(comps)
            );
        });
    }

    private boolean notCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            return p.getCountry() == null;
        }
        return true;
    }

    private List<String> getAvailable(ResearchCountry country, CPlayer p) {
        return ContinentalManagers.world(p.getInstance()).dataStorer().votingOption.getTree().getAvailable(country);
    }
}
