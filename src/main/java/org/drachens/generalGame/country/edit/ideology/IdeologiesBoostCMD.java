package org.drachens.generalGame.country.edit.ideology;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.enums.IdeologiesEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.additional.IdeologyGain;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class IdeologiesBoostCMD extends Command {
    private final HashMap<String, IdeologiesEnum> ideologiesEnumHashMap = new HashMap<>();

    public IdeologiesBoostCMD() {
        super("boost");

        this.ideologiesEnumHashMap.put("Fascist", IdeologiesEnum.ww2_fascist);
        this.ideologiesEnumHashMap.put("Anarchism", IdeologiesEnum.ww2_anarchist);
        this.ideologiesEnumHashMap.put("Conservative", IdeologiesEnum.ww2_conservatist);
        this.ideologiesEnumHashMap.put("Capitalist", IdeologiesEnum.ww2_capitalist);
        this.ideologiesEnumHashMap.put("Liberalist", IdeologiesEnum.ww2_liberalist);


        final Argument<String> ideologies = ArgumentType.String("ideology")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof final CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getIdeology().getIdeologyNames());
                });

        this.setDefaultExecutor((sender, context) -> sender.sendMessage(Component.text("Proper usage: /country edit ideology boost <ideology>", NamedTextColor.RED)));

        this.addSyntax((sender, context) -> {
            final CPlayer p = (CPlayer) sender;
            final Country country = p.getCountry();
            final IdeologiesEnum i = this.ideologiesEnumHashMap.get(context.get(ideologies));
            if (null == i.getIdeologyTypes()) {
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
}