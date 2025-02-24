package org.drachens.cmd.gamecreate.start;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.customgame.IntermissionGameWorld;
import org.drachens.player_types.CPlayer;

public class NewStartGameCMD extends Command {
    public NewStartGameCMD() {
        super("new");
        ArgumentWord name = ArgumentType.Word("name");

        addSyntax((sender, context)->{
            CPlayer p = (CPlayer) sender;
            String nam = context.get(name);
            if (!ContinentalManagers.chatCensor.isOkay(nam)){
                p.sendMessage(Component.text("Your input didn't pass the chat censor", NamedTextColor.RED));
                return;
            }
            new IntermissionGameWorld(p,nam);
        },name);
    }
}
