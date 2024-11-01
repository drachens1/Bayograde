package dev.ng5m;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.dataClasses.Countries.Country;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CPlayer extends Player {
    private Country country;
    private UUID lastMessenger;

    public CPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }


    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public UUID getLastMessenger() {
        return this.lastMessenger;
    }

    public void setLastMessenger(UUID player) {
        this.lastMessenger = player;
    }

    public void setLastMessenger(Player player) {
        this.lastMessenger = player.getUuid();
    }

}
