package dev.ng5m;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.dataClasses.Countries.Country;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CPlayer extends Player {
    private Country country;

    private UUID lastMessenger;

    private int gems;

    private final List<String> ownedCosmetics = new ArrayList<>();

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

    public int getGems(){
        return gems;
    }

    public void addCosmetic(String identifier){
        ownedCosmetics.add(identifier);
    }

    public boolean hasCosmetic(String identifier){
        return ownedCosmetics.contains(identifier);
    }
}
