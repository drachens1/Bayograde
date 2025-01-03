package dev.ng5m.bansystem;

import dev.ng5m.Constants;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.events.EventHandlerProviderManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;

public class BanSystemEvents implements EventHandlerProvider {

    static {
        EventHandlerProviderManager.registerProvider(BanSystemEvents.class);
    }

    @EventHandler
    public static void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (Constants.BAN_MANAGER.isBanned(event.getGameProfile().uuid())) {
            Player p = event.getConnection().getPlayer();
            p.kick(Constants.BAN_MANAGER.getBanMessage().apply(p));
        }
    }

}
