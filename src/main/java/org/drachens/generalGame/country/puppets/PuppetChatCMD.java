package org.drachens.generalGame.country.puppets;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Diplomacy.PuppetChat;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.HashMap;

public class PuppetChatCMD extends Command {
    private final HashMap<CPlayer, Boolean> active = new HashMap<>();

    public PuppetChatCMD() {
        super("chat");

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            PuppetChat puppetChat = p.getCountry().getInfo().getOverlord().getInfo().getPuppetChat();
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
}
