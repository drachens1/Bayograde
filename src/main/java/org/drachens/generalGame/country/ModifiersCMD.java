package org.drachens.generalGame.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.additional.ModifierCommand;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class ModifiersCMD extends Command {
    public ModifiersCMD() {
        super("modifiers");

        var modifier = ArgumentType.String("modifier")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    getSuggestionBasedOnInput(suggestion, country.getEconomy().getModifierNames());
                });

        var second = ArgumentType.String("input")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    ModifierCommand modifierCommand = country.getEconomy().getModifierCommmand(context.get(modifier));
                    if (modifierCommand == null) {
                        p.sendMessage("You do not have that modifier");
                        return;
                    }
                    modifierCommand.getSuggestion(p, context, suggestion);
                });

        setCondition((sender, s) -> !notInCountry(sender));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Proper usage /country modifiers <modifier>");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ModifierCommand modifierCommand = country.getEconomy().getModifierCommmand(context.get(modifier));
            if (modifierCommand == null) {
                p.sendMessage("You do not have that modifier");
                return;
            }
            modifierCommand.properUsage(p, context);
        }, modifier);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ModifierCommand modifierCommand = country.getEconomy().getModifierCommmand(context.get(modifier));
            if (modifierCommand == null) {
                p.sendMessage("You do not have that modifier");
                return;
            }
            modifierCommand.execute(p, context.get(second));
        }, modifier, second);
    }

    private boolean notInCountry(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return p.getCountry() == null;
    }
}
