package org.drachens.cmd.Dev.debug.CountryTypes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.CountryEnums;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.KyoriUtil.mergeComp;
import static org.drachens.util.ServerUtil.getWorldClasses;

public class CountryHistoryCMD extends Command {
    public CountryHistoryCMD() {
        super("countryhistory");
        setCondition((sender, permissionName) ->  sender.hasPermission("debug"));
        var type = ArgumentType.String("previousColonies | colony | colonialPower | upAndComing");
        type.setSuggestionCallback((sender, context, suggestion) -> {
            if (!sender.hasPermission("debug"))return;
            suggestion.addEntry(new SuggestionEntry("previousColonies"));
            suggestion.addEntry(new SuggestionEntry("colony"));
            suggestion.addEntry(new SuggestionEntry("colonialPower"));
            suggestion.addEntry(new SuggestionEntry("upAndComing"));
        });
        addSyntax((sender,context)->{
            if (!(sender instanceof Player p))return;
            if (!sender.hasPermission("debug"))return;
            List<Component> components  = new ArrayList<>();
            CountryEnums.History choice = CountryEnums.History.valueOf(context.get(type));
            components.add(compBuild(context.get(type), NamedTextColor.BLUE));
            components.add(Component.newline());
            for (Country country : getWorldClasses(p.getInstance()).countryDataManager().getCountries()){
                if (country.getHistory().equals(choice)){
                    components.add(compBuild(country.getName(), NamedTextColor.GOLD));
                    components.add(compBuild(", ", NamedTextColor.BLUE));
                }
            }
            p.sendMessage(mergeComp(components));
        },type);
    }
}
