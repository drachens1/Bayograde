package org.drachens.cmd.settings.premium.suffix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.player_types.CPlayer;

public class SuffixCMD extends Command {
    public SuffixCMD() {
        super("suffix");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        });

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.sendMessage(Component.text()
                    .append(Component.text("Suffix: ", NamedTextColor.GREEN))
                    .append(Component.text(p.getPlayerJson().getSuffix()))
                    .appendNewline()
                    .append(Component.text("Active: ", NamedTextColor.GREEN))
                    .append(Component.text(p.getPlayerJson().isSuffixActive()))
                    .build());
        });

        Component help = Component.text()
                .appendNewline()
                .append(Component.text("Suffix: "))
                .appendNewline()
                .append(Component.text("Requires: Deratus rank or Legatus rank"))
                .appendNewline()
                .append(Component.text("Commands:"))
                .appendNewline()
                .append(Component.text("/settings suffix set <name>   #Sets the suffix"))
                .appendNewline()
                .append(Component.text("/settings suffix toggle     #Toggles whether or not it is active"))
                .appendNewline()
                .append(Component.text("/settings suffix     #Displays the current settings"))
                .appendNewline()
                .build();

        addSubcommand(new SetSuffixCMD());
        addSubcommand(new ToggleSuffixCMD());
        addSubcommand(new CommandCreator("help")
                .setDefaultExecutor((sender,context)->sender.sendMessage(help))
                .build());
    }
}
