package org.drachens.cmd.settings.premium.login;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.player_types.CPlayer;

public class ToggleLoginMessageCMD extends Command {
    public ToggleLoginMessageCMD() {
        super("toggle");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasRank(RankEnum.deratus.getRank());
        });

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasRank(RankEnum.deratus.getRank()))return;
            boolean current = !p.getPlayerJson().isAutoVoteActive();
            p.getPlayerJson().setLoginMessageActive(current);
            p.sendMessage(Component.text()
                    .append(Component.text("Active: ", NamedTextColor.GREEN))
                    .append(Component.text(current))
                    .build());
        }));
    }
}
