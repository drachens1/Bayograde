package org.drachens.cmd.settings.premium.suffix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class SuffixCMD extends Command {
    public SuffixCMD() {
        super("suffix");

        setCondition(((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        }));

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.isPremium())return;
            p.sendMessage(Component.text()
                    .append(Component.text("Suffix: ", NamedTextColor.GREEN))
                    .append(Component.text(p.getPlayerJson().getSuffix()))
                    .appendNewline()
                    .append(Component.text("Active: ", NamedTextColor.GREEN))
                    .append(Component.text(p.getPlayerJson().isSuffixActive()))
                    .build());
        }));

        addSubcommand(new HelpSuffixCMD());
        addSubcommand(new SetSuffixCMD());
        addSubcommand(new ToggleSuffixCMD());
    }
}
