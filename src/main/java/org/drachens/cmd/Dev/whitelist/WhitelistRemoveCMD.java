package org.drachens.cmd.Dev.whitelist;

import org.drachens.player_types.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.customTypes.WhitelistFile;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;


public class WhitelistRemoveCMD extends Command {
    public WhitelistRemoveCMD() {
        super("remove");
        WhitelistFile whitelistFile = ContinentalManagers.configFileManager.getWhitelistFile();

        var player = ArgumentType.String("player");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("whitelist");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("whitelist")) sender.sendMessage("Usage /whitelist remove <player>");
        });
        addSyntax((sender, context) -> {
            UUID p = getUUIDFromName(context.get(player));
            if (p==null){
                sender.sendMessage(Component.text("Player not found", NamedTextColor.RED));
                return;
            }
            whitelistFile.removePlayer(p.toString());
            sender.sendMessage(context.get(player) + " was removed from the whitelist");
        }, player);
    }
}
