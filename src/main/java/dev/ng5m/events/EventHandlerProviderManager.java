package dev.ng5m.events;

import dev.ng5m.Util;
import dev.ng5m.bansystem.BanSystemEvents;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class EventHandlerProviderManager {

    public static void registerProvider(Class<? extends EventHandlerProvider> provider, Object ... additionalCtorArgs) {
        if (MinecraftServer.process() == null)
            throw new UnsupportedOperationException("You need to MinecraftServer#init before registering providers");

        for (Method method : provider.getDeclaredMethods()) {
            var mods = method.getModifiers();

            if (Modifier.isPrivate(mods)) continue;

            System.out.println(method.getName());

            if (!method.isAnnotationPresent(EventHandler.class)) continue;
//            var annotation = method.getAnnotation(EventHandler.class);
            var args = method.getParameterTypes();
            if (args.length != 1) continue;

            if (!Event.class.isAssignableFrom(args[0])) continue;

            var wrapper = new Wrapper<>((Class<? extends Event>) args[0]);

            MinecraftServer.getGlobalEventHandler().addListener(wrapper.eventClass(), event -> {
                try {
                    if (Modifier.isStatic(mods))
                        method.invoke(null, event);
                    else {
                        var instance = provider.getDeclaredConstructor(Util.toTypes(additionalCtorArgs)).newInstance(additionalCtorArgs);
                        method.invoke(instance, event);
                    }
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

    private record Wrapper<E extends Event>(Class<E> eventClass) {

    }

}
