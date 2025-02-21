package org.drachens.dataClasses.Diplomacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerChatEvent;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Channel;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

public class PuppetChat implements Channel {
    private final Country overlord;

    public PuppetChat(Country country) {
        this.overlord = country;
    }

    @Override
    public void onChat(PlayerChatEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        if (overlord == p.getCountry() || overlord.getDiplomacy().getPuppets().contains(p.getCountry())) {
            Component msg = Component.text()
                    .append(MessageEnum.puppetChat.getComponent())
                    .append(Component.text(p.getUsername()))
                    .append(Component.text(": "))
                    .append(Component.text(e.getRawMessage()))
                    .build();
            overlord.sendMessage(msg);
            overlord.getDiplomacy().getPuppets().forEach(puppet -> puppet.sendMessage(msg));
        } else {
            p.sendMessage(Component.text("You no longer have access to this chat", NamedTextColor.RED));
        }
    }

    public void addPlayer(CPlayer p) {
        ContinentalManagers.channelManager.putPlayer(p, this);
    }

    public void removePlayer(CPlayer p) {
        ContinentalManagers.channelManager.removePlayer(p);
    }
}
