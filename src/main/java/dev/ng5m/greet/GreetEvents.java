package dev.ng5m.greet;

import dev.ng5m.Util;
import dev.ng5m.cosmetic.gui.PerPlayerCosmeticsGUI;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.util.Configurable;
import dev.ng5m.util.Hookable;
import dev.ng5m.util.Settings;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.drachens.Manager.defaults.ContinentalManagers;

import java.util.function.Function;

public class GreetEvents extends Configurable<GreetEvents.GreetSettings> implements EventHandlerProvider,
        Hookable<GreetEvents, GreetEvents.GreetSettings> {

    public static GreetSettings settings;

    public GreetEvents() {

    }

    public GreetEvents(GreetSettings settings) {
        super(settings);
    }

    @EventHandler
    public void onPlayerJoin(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        Util.broadcast(settings.joinMessage.apply(event));

        var a = new PerPlayerCosmeticsGUI();
        a.decorate(event.getPlayer());
        ContinentalManagers.guiManager.openGUI(a, event.getPlayer());

    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        Util.broadcast(settings.leaveMessage.apply(event));
    }

    @Override
    public GreetEvents hook(GreetSettings settings) {
        GreetEvents.settings = settings;
        EventHandlerProviderManager.registerProvider(getClass(), settings);

        return new GreetEvents(settings);
    }

    public record GreetSettings(Function<PlayerSpawnEvent, Component> joinMessage,
                                Function<PlayerDisconnectEvent, Component> leaveMessage) implements Settings {
    }


}
