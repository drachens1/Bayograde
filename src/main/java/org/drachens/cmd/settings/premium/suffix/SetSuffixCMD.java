package org.drachens.cmd.settings.premium.suffix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.ChatCensor;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class SetSuffixCMD extends Command {
     public SetSuffixCMD() {
        super("set");

         ChatCensor chatCensor = ContinentalManagers.chatCensor;

         ArgumentString suffix = ArgumentType.String("name");

         addSyntax((sender, context)->{
             CPlayer p = (CPlayer) sender;
             String msg = context.get(suffix);
             if (!chatCensor.isOkay(msg)) {
                 p.sendMessage("Nickname did not pass the filter");
                 return;
             }
             p.getPlayerJson().setSuffix(msg);
             p.sendMessage(Component.text()
                             .append(Component.text("New Suffix: ", NamedTextColor.GREEN))
                             .append(MiniMessage.miniMessage().deserialize(msg))
                     .build());
         },suffix);
    }
}
