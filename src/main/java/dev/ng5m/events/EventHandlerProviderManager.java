package dev.ng5m.events;

import dev.ng5m.bansystem.BanSystemEvents;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class EventHandlerProviderManager {

    public static void registerProvider(Class<? extends EventHandlerProvider> provider) {
        if (MinecraftServer.process() == null)
            throw new UnsupportedOperationException("You need to MinecraftServer#init before registering providers");

        for (Method method : provider.getDeclaredMethods()) {
            var mods = method.getModifiers();

            if (!(((mods & Modifier.STATIC) != 0) && ((mods & Modifier.PRIVATE) == 0))) continue;
            System.out.println(method.getName());
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            var annotation = method.getAnnotation(EventHandler.class);
            var args = method.getParameterTypes();
            if (args.length != 1) continue;

            if (!Event.class.isAssignableFrom(annotation.clazz())) continue;

            MinecraftServer.getGlobalEventHandler().addListener(annotation.clazz(), event -> {
                try {
                    method.invoke(null, event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @SafeVarargs
    public static void registerProviders(Class<? extends EventHandlerProvider> ... providers) {
        for (var provider : providers) {
            registerProvider(provider);
        }

    }

    public static void hook() {
        registerProviders(
                BanSystemEvents.class
        );
    }

}
