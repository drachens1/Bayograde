package org.drachens.cmd.settings.premium.nickname;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.ChatCensor;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class SetNickNameCMD extends Command {
    public SetNickNameCMD() {
        super("set");

        ChatCensor chatCensor = ContinentalManagers.chatCensor;

        ArgumentString nick = ArgumentType.String("name");

        addSyntax((sender, context)->{
            CPlayer p = (CPlayer) sender;
            String msg = context.get(nick);
            if (!chatCensor.isOkay(msg)) {
                p.sendMessage("Nickname did not pass the filter");
                return;
            }
            p.getPlayerJson().setNickName(msg);
            p.sendMessage(Component.text()
                    .append(Component.text("New nickname: ", NamedTextColor.GREEN))
                    .append(MiniMessage.miniMessage().deserialize(msg))
                    .build());
        },nick);
    }
}
