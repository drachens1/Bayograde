package org.drachens.cmd.settings.premium.login;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import org.drachens.Manager.ChatCensor;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.customTypes.player.CustomLoginRecord;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.arrayToSentence;

public class SetLoginMessageCMD extends Command {
    public SetLoginMessageCMD() {
        super("set");

        ChatCensor chatCensor = ContinentalManagers.chatCensor;

        ArgumentWord option = ArgumentType.Word("option")
                .from("join", "change-world-join", "change-world-leave", "leave");

        ArgumentStringArray loginMessage = ArgumentType.StringArray("message");

        setDefaultExecutor((sender, context) -> sender.sendMessage(Component.text("If you need help do /settings login-message help", NamedTextColor.RED)));

        addSyntax((sender, context) -> {}, option);

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;

            String msg = arrayToSentence(context.get(loginMessage));
            if (!chatCensor.isOkay(msg)) {
                p.sendMessage("Join message did not pass the filter");
                return;
            }

            CustomLoginRecord clr = p.getPlayerJson().getCustomLoginMessage();
            if (null == clr) {
                clr = new CustomLoginRecord("", "", "", "");
            }

            switch (context.get(option)) {
                case "join":
                    clr = new CustomLoginRecord(msg, safeValue(clr.changeJoin()), safeValue(clr.changeLeave()), safeValue(clr.leave()));
                    break;
                case "change-world-join":
                    clr = new CustomLoginRecord(safeValue(clr.join()), msg, safeValue(clr.changeLeave()), safeValue(clr.leave()));
                    break;
                case "change-world-leave":
                    clr = new CustomLoginRecord(safeValue(clr.join()), safeValue(clr.changeJoin()), msg, safeValue(clr.leave()));
                    break;
                case "leave":
                    clr = new CustomLoginRecord(safeValue(clr.join()), safeValue(clr.changeJoin()), safeValue(clr.changeLeave()), msg);
                    break;
                default:
                    p.sendMessage("Enter a valid option");
                    return;
            }

            p.sendMessage(clr.getComponent());
            p.getPlayerJson().setCustomLoginMessage(clr);
        }, option, loginMessage);
    }

    private String safeValue(String value) {
        return (null != value) ? value : "";
    }
}
