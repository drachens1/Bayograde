package org.drachens.temporary.country.research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ColoursEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.player_types.CPlayer;
import org.drachens.dataClasses.Research.ResearchCountry;

import java.util.ArrayList;
import java.util.List;

public class ResearchTechTreeCMD extends Command {
    public ResearchTechTreeCMD() {
        super("full-tree");

        setCondition((sender, s) -> !notCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (notCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ResearchCountry researchCountry = country.getResearchCountry();
            TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
            List<Component> comps = new ArrayList<>();
            tree.getResearchCategories().forEach(researchCategory -> {
                comps.add(Component.text()
                        .append(researchCategory.getType())
                        .append(Component.text(" : "))
                        .appendNewline()
                        .build());
                researchCategory.getResearchOptionList().forEach(researchOption -> {
                    if (researchCountry.hasResearched(researchOption.getIdentifier())) {
                        comps.add(researchOption.getName().color(NamedTextColor.GREEN));
                    } else if (researchCountry.isResearching() && researchCountry.getCurrentResearch() == researchOption) {
                        comps.add(researchOption.getName().color(ColoursEnum.ORANGE.getTextColor()));
                    } else if (researchOption.canResearch(researchCountry)) {
                        comps.add(researchOption.getName().color(NamedTextColor.YELLOW));
                    } else {
                        comps.add(researchOption.getName().color(NamedTextColor.RED));
                    }
                    comps.add(Component.text(" -> "));
                });
                comps.removeLast();
                comps.add(Component.newline());
            });
            p.sendMessage(Component.text()
                    .append(Component.text("________________/", NamedTextColor.BLUE))
                    .append(Component.text("Full Tree"))
                    .append(Component.text("\\________________", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(comps)
                    .build());
        });
    }

    private boolean notCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            return p.getCountry() == null;
        }
        return true;
    }
}
