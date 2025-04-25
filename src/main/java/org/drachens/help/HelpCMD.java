package org.drachens.help;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public class HelpCMD extends Command {
    public HelpCMD() {
        super("help");

        Component generalInfo = Component.text()
                .append(Component.text("General help: "))
                .appendNewline()
                .append(Component.text("do /settings # to view the settings stuff"))
                .append(Component.text(""))
                        .build();

        setDefaultExecutor((sender,context)->{
            sender.sendMessage(generalInfo);
        });
    }
}
