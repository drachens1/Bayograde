package org.drachens.temporary.country.edit.ideology;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.enums.IdeologiesEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.IdeologyGain;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class IdeologiesBoostCMD extends Command {
    private final HashMap<String, IdeologiesEnum> ideologiesEnumHashMap = new HashMap<>();

    public IdeologiesBoostCMD() {
        super("boost");

        ideologiesEnumHashMap.put("Fascist", IdeologiesEnum.ww2_fascist);
        ideologiesEnumHashMap.put("Anarchism", IdeologiesEnum.ww2_anarchist);
        ideologiesEnumHashMap.put("Conservative", IdeologiesEnum.ww2_conservatist);
        ideologiesEnumHashMap.put("Capitalist", IdeologiesEnum.ww2_capitalist);
        ideologiesEnumHashMap.put("Liberalist", IdeologiesEnum.ww2_liberalist);


        var ideologies = ArgumentType.String("ideology")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) return;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getIdeology().getIdeologyNames());
                });

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        setDefaultExecutor((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            sender.sendMessage(Component.text("Proper usage: /country edit ideology boost <ideology>", NamedTextColor.RED));
        });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            IdeologiesEnum i = ideologiesEnumHashMap.get(context.get(ideologies));
            if (i.getIdeologyTypes() == null) {
                p.sendMessage(Component.text("Ideology was null"));
                return;
            }
            country.addEventsRunner(new IdeologyGain(country, i.getIdeologyTypes(), 0.5f, 70));
            p.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(i.getIdeologyTypes().getName())
                    .append(Component.text(" is increasing by 0.5 per day for 70 days", NamedTextColor.GREEN))
                    .build());
        }, ideologies);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}