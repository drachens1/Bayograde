package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerChatEvent;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.Channel;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

public class FactionChat implements Channel {
    private final Factions factions;

    public FactionChat(Factions factions) {
        this.factions = factions;
    }

    @Override
    public void onChat(PlayerChatEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        if (factions.containsCountry(p.getCountry())) {
            factions.sendMessage(Component.text()
                    .append(MessageEnum.factionChat.getComponent())
                    .append(Component.text(p.getUsername()))
                    .append(Component.text(": "))
                    .append(Component.text(e.getRawMessage()))
                    .build());
        } else {
            p.sendMessage(Component.text("You no longer have access to this chat", NamedTextColor.RED));
            removePlayer(p);
        }
    }

    public void addPlayer(CPlayer p) {
        ContinentalManagers.channelManager.putPlayer(p, this);
    }

    public void removePlayer(CPlayer p) {
        ContinentalManagers.channelManager.removePlayer(p);
    }
}
