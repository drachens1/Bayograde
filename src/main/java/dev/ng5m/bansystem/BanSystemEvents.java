package dev.ng5m.bansystem;

import dev.ng5m.Constants;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.events.EventHandlerProviderManager;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;

public class BanSystemEvents implements EventHandlerProvider {

    static {
        EventHandlerProviderManager.registerProvider(BanSystemEvents.class);
    }

    @EventHandler
    public static void onPreLogin(AsyncPlayerPreLoginEvent event) {
        var player = event.getPlayer();

        if (Constants.BAN_MANAGER.isBanned(player)) {
            player.kick(Constants.BAN_MANAGER.getBanMessage().apply(player));
        }
    }

}
