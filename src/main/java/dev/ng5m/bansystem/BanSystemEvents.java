package dev.ng5m.bansystem;

import dev.ng5m.Constants;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.events.EventHandlerProviderManager;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.network.player.GameProfile;

public class BanSystemEvents implements EventHandlerProvider {

    static {
        EventHandlerProviderManager.registerProvider(BanSystemEvents.class);
    }

    @EventHandler
    public static void onPreLogin(AsyncPlayerPreLoginEvent event) {
        GameProfile gameProfile = event.getGameProfile();

        if (Constants.BAN_MANAGER.isBanned(gameProfile.uuid())) {
            event.getConnection().getPlayer() .kick(Constants.BAN_MANAGER.getBanMessage().apply(event.getConnection().getPlayer()));
        }
    }

}
