package org.drachens.cmd.faction;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class FactionCMD extends Command {
    public FactionCMD() {
        super("faction");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction <command>"));

        addSubcommand(new JoinCMD());
        addSubcommand(new CreateCMD());
        addSubcommand(new DeleteCMD());
        addSubcommand(new Kick());

        var smth = ArgumentType.String("Factions")
                        .setSuggestionCallback((sender,context,suggestion)->{
                            Player p = (Player) sender;
                            p.refreshCommands();
                        });
        addSyntax((sender,context)->{},smth);
    }
}
