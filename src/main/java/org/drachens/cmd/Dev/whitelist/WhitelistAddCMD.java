package org.drachens.cmd.Dev.whitelist;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.customTypes.WhitelistFile;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class WhitelistAddCMD extends Command {
    public WhitelistAddCMD() {
        super("add");
        WhitelistFile whitelistFile = ContinentalManagers.configFileManager.getWhitelistFile();
        var player = ArgumentType.String("player");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("whitelist")) p.sendMessage("Usage /whitelist add <player>");
        });
        addSyntax((sender, context) -> {
            CPlayer player1 = (CPlayer) sender;
            if (!player1.hasPermission("whitelist")) return;
            UUID p = getUUIDFromName(context.get(player));
            if (p==null){
                sender.sendMessage(Component.text("Player not found", NamedTextColor.RED));
                return;
            }
            whitelistFile.removePlayer(p.toString());
            sender.sendMessage(context.get(player) + " was added to the whitelist");
        }, player);
    }
}
