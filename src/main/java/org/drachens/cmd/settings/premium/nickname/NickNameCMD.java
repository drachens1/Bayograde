package org.drachens.cmd.settings.premium.nickname;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.player_types.CPlayer;

public class NickNameCMD extends Command {
    public NickNameCMD() {
        super("nickname");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasRank(RankEnum.deratus.getRank());
        });

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.sendMessage(Component.text()
                            .append(Component.text("Nick: ", NamedTextColor.GREEN))
                            .append(MiniMessage.miniMessage().deserialize(p.getPlayerJson().getNickname()))
                            .appendNewline()
                            .append(Component.text("Active: ",NamedTextColor.GREEN))
                            .append(Component.text(p.getPlayerJson().isNicknameActive()))
                    .build());
        });

        Component help = Component.text()
                .appendNewline()
                .append(Component.text("Login Message: "))
                .appendNewline()
                .append(Component.text("Requires: Deratus rank"))
                .appendNewline()
                .append(Component.text("Commands:"))
                .appendNewline()
                .append(Component.text("/settings nickname set <name>   #Sets the nickname"))
                .appendNewline()
                .append(Component.text("/settings nickname toggle     #Toggles whether or not it is active"))
                .appendNewline()
                .append(Component.text("/settings nickname     #Displays the current settings"))
                .appendNewline()
                .build();

        addSubcommand(new SetNickNameCMD());
        addSubcommand(new ToggleNickNameCMD());
        addSubcommand(new CommandCreator("help")
                .setDefaultExecutor((sender,context)->sender.sendMessage(help))
                .build());
    }
}
