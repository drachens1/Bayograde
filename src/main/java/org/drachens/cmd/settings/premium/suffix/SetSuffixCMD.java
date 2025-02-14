package org.drachens.cmd.settings.premium.suffix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.player_types.CPlayer;

public class SetSuffixCMD extends Command {
     public SetSuffixCMD() {
        super("set");

         setCondition(((sender, commandString) -> {
             CPlayer p = (CPlayer) sender;
             return p.isPremium();
         }));

         var suffix = ArgumentType.String("name");

         addSyntax((sender,context)->{
             CPlayer p = (CPlayer) sender;
             if (!p.isPremium())return;
             p.getPlayerJson().setSuffix(context.get(suffix));
             p.sendMessage(Component.text()
                             .append(Component.text("New Suffix: ", NamedTextColor.GREEN))
                             .append(MiniMessage.miniMessage().deserialize(context.get(suffix)))
                     .build());
         },suffix);
    }
}
