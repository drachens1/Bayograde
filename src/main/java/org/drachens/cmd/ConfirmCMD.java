package org.drachens.cmd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;

public class ConfirmCMD extends Command {
    private final Component error = Component.text("You have nothing to confirm", NamedTextColor.RED);
    private final HashMap<CPlayer, Runnable> playerRunnableHashMap = new HashMap<>();

    public ConfirmCMD() {
        super("confirm");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            if (!playerRunnableHashMap.containsKey(p)) {
                p.sendMessage(error);
                return;
            }
            playerRunnableHashMap.get(p).run();
            playerRunnableHashMap.remove(p);
        });
    }

    public void putPlayerRunnable(CPlayer p, Runnable runnable) {
        playerRunnableHashMap.put(p, runnable);
    }
}
