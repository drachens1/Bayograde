package org.drachens.cmd.settings.premium.login;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.player_types.CPlayer;

public class LoginMessageCMD extends Command {
    public LoginMessageCMD() {
        super("login-message");

        setCondition((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        });

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.sendMessage(p.getPlayerJson().getCustomLoginMessage().getComponent());
        });

        Component help = Component.text()
                .appendNewline()
                .append(Component.text("Login Message: "))
                .appendNewline()
                .append(Component.text("Requires: Deratus rank or Legatus rank"))
                .appendNewline()
                .append(Component.text("Commands:"))
                .appendNewline()
                .append(Component.text("/settings login-message set <option> <message>   #Sets the login message"))
                .appendNewline()
                .append(Component.text("/settings login-message toggle     #Toggles whether or not it is active"))
                .appendNewline()
                .append(Component.text("/settings login-message     #Displays the current settings"))
                .appendNewline()
                .build();

        addSubcommand(new SetLoginMessageCMD());
        addSubcommand(new ToggleLoginMessageCMD());
        addSubcommand(new CommandCreator("help")
                .setDefaultExecutor(((sender, context) -> sender.sendMessage(help)))
                .build());
    }
}
