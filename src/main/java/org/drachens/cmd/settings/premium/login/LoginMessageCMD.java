package org.drachens.cmd.settings.premium.login;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class LoginMessageCMD extends Command {
    public LoginMessageCMD() {
        super("login-message");

        setCondition(((sender, commandString) -> {
            CPlayer p = (CPlayer) sender;
            return p.isPremium();
        }));

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.isPremium()){
                return;
            }
            p.sendMessage(p.getPlayerJson().getCustomLoginMessage().getComponent());
        }));

        addSubcommand(new SetLoginMessageCMD());
        addSubcommand(new HelpLoginMessageCMD());
        addSubcommand(new ToggleLoginMessageCMD());
    }
}
