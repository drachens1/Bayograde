package org.drachens.player_types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.Manager.defaults.enums.ClientSideExtras;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.fileManagement.customTypes.player.PlayerInfoEntry;
import org.drachens.fileManagement.customTypes.player.PlayerJson;
import org.drachens.store.other.Rank;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.drachens.util.OtherUtil.formatPlaytime;

public class CPlayer extends Player {
    private final List<String> permissions = new ArrayList<>();

    private final HashMap<ClientSideExtras, List<Clientside>> clientSides = new HashMap<>();
    private final PriorityQueue<Rank> ranks = new PriorityQueue<>(Comparator.comparingInt(Rank::getWeight).reversed());
    private final HashSet<String> ownedCosmetics = new HashSet<>();
    private Country country;

    private UUID lastMessenger;

    private ItemStack headItem;
    private LocalTime joinTime;
    private Long playTime;
    private LocalTime lastCheck;
    private PlayerJson playerJson;
    private PlayerInfoEntry playerInfoEntry;
    private boolean isUsingMod = false;

    public CPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public PlayerInfoEntry getPlayerInfoEntry() {
        return playerInfoEntry;
    }

    public void setIsUsingMod(boolean b) {
        isUsingMod = b;
    }

    public boolean isUsingMod() {
        return isUsingMod;
    }

    public void setPlayerDataFile(PlayerInfoEntry playerInfoEntry) {
        this.playerInfoEntry = playerInfoEntry;
    }
    
    public void setPlayerJson(PlayerJson playerJson){
        this.playerJson=playerJson;
    }

    public void setOriginalPlayTime(Long pt) {
        this.playTime = pt;
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
        playerJson.setPlaytime(playTime);
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

    public void addRank(Rank rank){
        ranks.add(rank);
        ownedCosmetics.addAll(rank.getCosmetics());
        playerJson.addRank(rank.getIdentifier());
        if (!rank.getIdentifier().equalsIgnoreCase("default_rank")){
            playerJson.setPremium(true);
        }
    }

    public void removeRank(Rank rank){
        ranks.remove(rank);
        rank.getCosmetics().forEach(ownedCosmetics::remove);
        playerJson.removeRank(rank.getIdentifier());
        if (!ranks.contains(RankEnum.deratus.getRank())||ranks.contains(RankEnum.legatus.getRank())){
            playerJson.setPremium(false);
        }
    }

    public boolean hasRank(Rank rank){
        return ranks.contains(rank);
    }

    public boolean hasCosmetic(String cosmetic){
        return ownedCosmetics.contains(cosmetic);
    }

    public ItemStack getPlayerHead() {
        return headItem;
    }

    public boolean hasPermission(@NotNull String permission) {
        return permissions.contains(permission);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(@NotNull String permission) {
        permissions.remove(permission);
    }

    public @NotNull List<String> getAllPermissionss() {
        return permissions;
    }

    public List<String> getCosmetics(){
        return new ArrayList<>(ownedCosmetics);
    }

    public void addClientSide(ClientSideExtras clientSideExtras, Clientside clientSide){
        List<Clientside> clientsides = clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
        clientsides.add(clientSide);
        clientSides.put(clientSideExtras,clientsides);
    }

    public void removeClientSide(ClientSideExtras clientSideExtras, Clientside clientSide){
        List<Clientside> clientsides = clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
        if (clientsides.remove(clientSide)){
            clientSides.put(clientSideExtras,clientsides);
        }else {
            clientSides.remove(clientSideExtras);
        }
    }

    public void addClientSides(ClientSideExtras clientSideExtras, List<Clientside> toAdd){
        List<Clientside> clientsides = clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
        clientsides.addAll(toAdd);
        clientSides.put(clientSideExtras,clientsides);
    }

    public void removeClientSides(ClientSideExtras clientSideExtras, List<Clientside> toRemove){
        List<Clientside> clientsides = clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
        clientsides.removeAll(toRemove);
        if (clientsides.isEmpty()){
            clientSides.remove(clientSideExtras);
        }else {
            clientSides.put(clientSideExtras,clientsides);
        }
    }

    public List<Clientside> getClientSideExtras(ClientSideExtras clientSideExtras){
        return clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
    }

    public void removeClientSideExtras(ClientSideExtras clientSideExtras){
        List<Clientside> clientsides = clientSides.getOrDefault(clientSideExtras,new ArrayList<>());
        clientsides.forEach(Clientside::dispose);
        this.clientSides.remove(clientSideExtras);
    }

    public PlayerJson getPlayerJson(){
        return playerJson;
    }

    public PriorityQueue<Rank> getRanks(){
        return ranks;
    }

    public Rank getDominantRank(){
        return ranks.peek();
    }

    public boolean isPremium(){
        return playerJson.isPremium();
    }
}
