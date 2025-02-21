package org.drachens.generalGame.country.research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class ResearchOptionCMD extends Command {
    public ResearchOptionCMD() {
        super("start");

        Argument<String> choice = ArgumentType.String("choice")
                .setSuggestionCallback((sender, context, suggestion) -> getSuggestionBasedOnInput(suggestion, getAvailable(sender)));

        setCondition((sender, s) -> !notCountry(sender));

        setDefaultExecutor((sender, context) -> sender.sendMessage(Component.text("Proper usage: /country research option <choice>", NamedTextColor.RED)));

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
            ResearchOption researchOption = tree.getResearchOption(context.get(choice));
            if (null == researchOption) {
                return;
            }
            if (researchOption.canResearch(country.getResearch().researchCountry())) {
                country.getResearch().researchCountry().startResearching(researchOption);
            } else {
                p.sendMessage(Component.text("You cannot research this", NamedTextColor.RED));
            }
        }, choice);
    }

    private boolean notCountry(CommandSender sender) {
        return !(sender instanceof CPlayer p) || (null == p.getCountry());
    }

    private List<String> getAvailable(CommandSender sender) {
        if (notCountry(sender)) return new ArrayList<>();
        CPlayer p = (CPlayer) sender;
        Country country = p.getCountry();
        TechTree tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        return tree.getAvailable(country.getResearch().researchCountry());
    }
}
