package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class GoldCMD extends Command {
    public GoldCMD() {
        super("gold");
        setCondition((sender,s)->sender.hasPermission("cheat"));
        var options = ArgumentType.String("choice")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!sender.hasPermission("cheat"))return;
                    suggestion.addEntry(new SuggestionEntry("add"));
                    suggestion.addEntry(new SuggestionEntry("remove"));
                });
        var amount = ArgumentType.Integer("Amount");

        addSyntax((sender,context)->{},options);
        addSyntax((sender,context)->{
            if (!sender.hasPermission("cheat"))return;
            CPlayer p = (CPlayer) sender;
            switch (context.get(options)){
                case "add":
                    p.addGold(context.get(amount));
                    p.sendMessage("You now have "+p.getGold());
                    break;
                case "remove":
                    p.minusGold(context.get(amount));
                    p.sendMessage("You now have "+p.getGold());
                    break;
            }
        },options,amount);

    }
}
