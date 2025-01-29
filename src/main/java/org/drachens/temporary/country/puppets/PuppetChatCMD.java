package org.drachens.temporary.country.puppets;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.PuppetChat;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

public class PuppetChatCMD extends Command {
    private final HashMap<CPlayer, Boolean> active = new HashMap<>();

    public PuppetChatCMD() {
        super("chat");

        setCondition((sender, s) -> isOrHasPuppets(sender));

        setDefaultExecutor((sender, context) -> {
            if (!isOrHasPuppets(sender)) return;
            CPlayer p = (CPlayer) sender;
            PuppetChat puppetChat = p.getCountry().getOverlord().getPuppetChat();
            boolean current = active.getOrDefault(p, false);
            current = !current;
            active.put(p, current);
            if (current) {
                puppetChat.addPlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(Component.text("Puppet chat is now active")));
            } else {
                puppetChat.removePlayer(p);
                p.sendMessage(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(Component.text("Puppet chat is now inactive")));
            }
        });
    }

    private boolean isOrHasPuppets(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && (country.hasOverlord() || country.hasPuppets());
        }
        return false;
    }
}
