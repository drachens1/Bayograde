package org.drachens.temporary.country.research;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class ResearchOptionCMD extends Command {
    public ResearchOptionCMD() {
        super("start");

        var choice = ArgumentType.String("choice")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    getSuggestionBasedOnInput(suggestion, getAvailable(sender));
                });

        setCondition((sender, s) -> !notCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (notCountry(sender)) return;
            sender.sendMessage(Component.text("Proper usage: /country research option <choice>", NamedTextColor.RED));
        });

        addSyntax((sender, context) -> {
            if (notCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            ResearchCountry country = (ResearchCountry) p.getCountry();
            TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
            ResearchOption researchOption = tree.getResearchOption(context.get(choice));
            if (researchOption == null) {
                return;
            }
            if (researchOption.canResearch(country)) {
                country.startResearching(researchOption);
            } else {
                p.sendMessage(Component.text("You cannot research this", NamedTextColor.RED));
            }
        }, choice);
    }

    private boolean notCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            return p.getCountry() == null;
        }
        return true;
    }

    private List<String> getAvailable(CommandSender sender) {
        if (notCountry(sender)) return new ArrayList<>();
        CPlayer p = (CPlayer) sender;
        ResearchCountry country = (ResearchCountry) p.getCountry();
        TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        return tree.getAvailable(country);
    }
}
