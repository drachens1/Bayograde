package org.drachens.cmd.settings.premium.nickname;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class ToggleNickNameCMD extends Command {
    public ToggleNickNameCMD() {
        super("toggle");
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            boolean current = !p.getPlayerJson().isNicknameActive();
            p.getPlayerJson().setNickNameActive(current);
            p.sendMessage(Component.text()
                    .append(Component.text("Active: ", NamedTextColor.GREEN))
                    .append(Component.text(current))
                    .build());
        });
    }
}
