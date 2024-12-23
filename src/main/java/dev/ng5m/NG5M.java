package dev.ng5m;

import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.greet.GreetEvents;
import net.minestom.server.MinecraftServer;
import org.drachens.miniGameSystem.minigames.FlappyBird;

public class NG5M {

    public static void hook() {
        MinecraftServer.getConnectionManager().setPlayerProvider(CPlayer::new);

        EventHandlerProviderManager.registerProvider(FlappyBird.class);

        new GreetEvents().hook(
                new GreetEvents.GreetSettings(
                        event ->
                                Util.colored("[", Constants.Colors.LIGHT_GRAY).append(
                                        Util.colored("+", Constants.Colors.LIME).append(
                                                Util.colored("] " + event.getPlayer().getUsername(), Constants.Colors.LIGHT_GRAY)
                                        )
                                ),
                        event ->
                                Util.colored("[", Constants.Colors.LIGHT_GRAY).append(
                                        Util.colored("-", Constants.Colors.RED).append(
                                                Util.colored("]" + event.getPlayer().getUsername(), Constants.Colors.LIGHT_GRAY)
                                        )
                                ))
        );

        try {

            if (!FlappyBird.db.exists()) {
                FlappyBird.db.createNewFile();

                Util.writeString(FlappyBird.db, "{}");
            }

        } catch (Exception x) {
            throw new RuntimeException(x);
        }


    }

}
