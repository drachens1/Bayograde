package dev.ng5m;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.fileManagement.PlayerInfoEntry;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.drachens.util.OtherUtil.formatPlaytime;

public class CPlayer extends Player {
    private final List<String> ownedCosmetics = new ArrayList<>();
    private Country country;

    private UUID lastMessenger;

    private int gold = 0;
    private ItemStack headItem;
    private LocalTime joinTime;
    private Long playTime;
    private LocalTime lastCheck;
    private PlayerInfoEntry playerInfoEntry;
    private final List<Clientside> clientsides = new ArrayList<>();

    public CPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    public PlayerInfoEntry getPlayerInfoEntry() {
        return playerInfoEntry;
    }

    public void setPlayerDataFile(PlayerInfoEntry playerInfoEntry) {
        this.playerInfoEntry = playerInfoEntry;
    }

    public void setPlayTime(Long pt) {
        this.playTime = pt;
        playerInfoEntry.setPlaytime(playTime);
    }

    public void addPlayTime(LocalTime localTime) {
        if (lastCheck == null)
            lastCheck = joinTime;

        if (playTime == null) {
            playTime = 0L;
            lastCheck = localTime;
        } else {
            playTime += Duration.between(lastCheck, localTime).toSeconds();
            lastCheck = localTime;
        }
        playerInfoEntry.setPlaytime(playTime);
    }

    public void setJoinTime(LocalTime instant) {
        this.joinTime = instant;
    }

    public String getPlayTimeString() {
        return formatPlaytime(playTime);
    }

    public void setHead() {
        PlayerSkin playerSkin = getSkin();
        if (playerSkin == null) {
            System.err.println("Players skin was null!");
            return;
        }
        headItem = ItemStack.builder(Material.PLAYER_HEAD).set(ItemComponent.PROFILE, new HeadProfile(playerSkin))
                .customName(Component.text(getUsername(), NamedTextColor.GOLD))
                .build();
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

    public void setLastMessenger(Player player) {
        this.lastMessenger = player.getUuid();
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
        playerInfoEntry.setGold(gold);
    }

    public void minusGold(int amount) {
        gold -= amount;
        playerInfoEntry.setGold(gold);
    }

    public void addGold(int amount) {
        gold += amount;
        playerInfoEntry.setGold(gold);
    }

    public void addCosmetic(String identifier) {
        ownedCosmetics.add(identifier);
    }

    public void removeCosmetic(String identifier) {
        ownedCosmetics.remove(identifier);
    }

    public boolean hasCosmetic(String identifier) {
        return ownedCosmetics.contains(identifier);
    }

    public List<String> getOwnedCosmetics() {
        return ownedCosmetics;
    }

    public ItemStack getPlayerHead() {
        return headItem;
    }

    public List<Clientside> getClientsides() {
        return clientsides;
    }

    public void addClientside(Clientside clientside) {
        clientsides.add(clientside);
    }

    public void removeClientside(Clientside clientside) {
        clientsides.remove(clientside);
    }
}
