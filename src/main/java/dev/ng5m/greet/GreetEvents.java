package dev.ng5m.greet;

import dev.ng5m.Util;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.util.Configurable;
import dev.ng5m.util.Hookable;
import dev.ng5m.util.Settings;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.function.Function;

public class GreetEvents extends Configurable<GreetEvents.GreetSettings> implements EventHandlerProvider,
        Hookable<GreetEvents, GreetEvents.GreetSettings> {

    public GreetEvents(GreetSettings settings) {
        super(settings);
    }

    @EventHandler
    public void onPlayerJoin(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        Util.broadcast(this.settings.joinMessage.apply(event));
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        Util.broadcast(this.settings.leaveMessage.apply(event));
    }

    @Override
    public GreetEvents hook(GreetSettings settings) {
        EventHandlerProviderManager.registerProvider(getClass());

        return new GreetEvents(settings);
    }

    public record GreetSettings(Function<PlayerSpawnEvent, Component> joinMessage,
                                Function<PlayerDisconnectEvent, Component> leaveMessage) implements Settings {
    }


}
