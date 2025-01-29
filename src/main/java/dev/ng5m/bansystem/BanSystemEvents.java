package dev.ng5m.bansystem;

import dev.ng5m.Constants;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;

public class BanSystemEvents  {
    public BanSystemEvents(){
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerPreLoginEvent.class, e->{
            if (Constants.BAN_MANAGER.isBanned(e.getGameProfile().uuid())) {
                Player p = e.getConnection().getPlayer();
                if (p==null)return;
                p.kick(Constants.BAN_MANAGER.getBanMessage().apply(p));
            }
        });
    }
}
