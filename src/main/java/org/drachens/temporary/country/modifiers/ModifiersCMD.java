package org.drachens.temporary.country.modifiers;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.ModifierCommand;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class ModifiersCMD extends Command {
    public ModifiersCMD() {
        super("modifiers");

        var modifier = ArgumentType.String("modifier")
                        .setSuggestionCallback((sender,context,suggestion)->{
                            if (notInCountry(sender))return;
                            CPlayer p = (CPlayer) sender;
                            Country country = p.getCountry();
                            getSuggestionBasedOnInput(suggestion,country.getModifierNames());
                        });

        var second = ArgumentType.String("input")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (notInCountry(sender))return;
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    ModifierCommand modifierCommand = country.getModifierCommand(context.get(modifier));
                    if (modifierCommand==null){
                        p.sendMessage("You do not have that modifier");
                        return;
                    }
                    modifierCommand.getSuggestion(p,context,suggestion);
                });

        setCondition((sender,s)->!notInCountry(sender));

        setDefaultExecutor((sender,context)->{
            if (notInCountry(sender))return;
            sender.sendMessage("Proper usage /country modifiers <modifier>");
        });

        addSyntax((sender,context)->{
            if (notInCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ModifierCommand modifierCommand = country.getModifierCommand(context.get(modifier));
            if (modifierCommand==null){
                p.sendMessage("You do not have that modifier");
                return;
            }
            modifierCommand.properUsage(p,context);
        },modifier);

        addSyntax((sender,context)->{
            if (notInCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            ModifierCommand modifierCommand = country.getModifierCommand(context.get(modifier));
            if (modifierCommand==null){
                p.sendMessage("You do not have that modifier");
                return;
            }
            modifierCommand.execute(p,context.get(second));
        },modifier,second);
    }

    private boolean notInCountry(CommandSender sender){
        CPlayer p = (CPlayer) sender;
        return p.getCountry()==null;
    }
}
