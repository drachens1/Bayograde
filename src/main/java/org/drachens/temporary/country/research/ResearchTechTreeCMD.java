package org.drachens.temporary.country.research;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ColoursEnum;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.List;

public class ResearchTechTreeCMD extends Command {
    public ResearchTechTreeCMD() {
        super("full-tree");

        setCondition((sender,s)->!notCountry(sender));

        setDefaultExecutor((sender,context)->{
            if (notCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            ResearchCountry country = (ResearchCountry) p.getCountry();
            TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
            List<Component> comps = new ArrayList<>();
            tree.getResearchCategories().forEach(researchCategory -> {
                comps.add(Component.text()
                                .append(researchCategory.getType())
                                .append(Component.text(" : "))
                                .appendNewline()
                        .build());
                researchCategory.getResearchOptionList().forEach(researchOption -> {
                    if (country.hasResearched(researchOption.getIdentifier())){
                        comps.add(researchOption.getName().color(NamedTextColor.GREEN));
                    } else if (country.isResearching()&&country.getCurrentResearch()==researchOption) {
                        comps.add(researchOption.getName().color(ColoursEnum.ORANGE.getTextColor()));
                    }else if (researchOption.canResearch(country)){
                        comps.add(researchOption.getName().color(NamedTextColor.YELLOW));
                    }else {
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

    private boolean notCountry(CommandSender sender){
        if (sender instanceof CPlayer p){
            return p.getCountry()==null;
        }
        return true;
    }
}
