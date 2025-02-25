package org.drachens.player_types;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ClientSideExtras;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.datastorage.WorldClasses;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.fileManagement.customTypes.player.PlayerInfoEntry;
import org.drachens.fileManagement.customTypes.player.PlayerJson;
import org.drachens.store.other.Rank;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.drachens.util.OtherUtil.formatPlaytime;

@Getter
@Setter
public class CPlayer extends Player {
    private final List<String> permissions = new ArrayList<>();

    private final HashMap<ClientSideExtras, List<Clientside>> clientSides = new HashMap<>();
    private final PriorityQueue<Rank> ranks = new PriorityQueue<>(Comparator.comparingInt(Rank::getWeight).reversed());
    private final HashSet<String> ownedCosmetics = new HashSet<>();
    private Country country;

    private UUID lastMessenger;

    private WorldClasses worldClasses;
    private ItemStack headItem;
    private LocalTime joinTime;
    private Long playTime;
    private LocalTime lastCheck;
    private PlayerJson playerJson;
    private PlayerInfoEntry playerInfoEntry;
    private boolean isUsingMod;
    private boolean leaderOfOwnGame;
    private boolean isInOwnGame;
    private boolean isInIntermission;

    public CPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public boolean isUsingMod() {
        return isUsingMod;
    }

    public void setPlayerDataFile(PlayerInfoEntry playerInfoEntry) {
        this.playerInfoEntry = playerInfoEntry;
    }

    public void setOriginalPlayTime(Long pt) {
        this.playTime = pt;
    }

    public void addPlayTime(LocalTime localTime) {
        if (null == this.lastCheck) lastCheck = joinTime;

        if (null == this.playTime) {
            playTime = 0L;
        } else {
            playTime += Duration.between(lastCheck, localTime).toSeconds();
        }
        lastCheck = localTime;
        playerJson.setPlaytime(playTime);
    }

    public String getPlayTimeString() {
        return formatPlaytime(playTime);
    }

    public void setHead() {
        PlayerSkin playerSkin = getSkin();
        if (null == playerSkin) {
            System.err.println("Players skin was null!");
            return;
        }
        headItem = ItemStack.builder(Material.PLAYER_HEAD).set(ItemComponent.PROFILE, new HeadProfile(playerSkin))
                .customName(Component.text(getUsername(), NamedTextColor.GOLD))
                .build();
    }

    public void setLastMessenger(Player player) {
        this.lastMessenger = player.getUuid();
    }

    public void addRank(Rank rank){
        ranks.add(rank);
        ownedCosmetics.addAll(rank.getCosmetics());
        playerJson.addRank(rank.getIdentifier());
        if (!"default_rank".equalsIgnoreCase(rank.getIdentifier())){
            playerJson.setPremium(true);
        }
        refreshCommands();
        ContinentalManagers.tabManager.updatePlayer(this);
    }

    public void removeRank(Rank rank){
        ranks.remove(rank);
        rank.getCosmetics().forEach(ownedCosmetics::remove);
        playerJson.removeRank(rank.getIdentifier());
        if (!ranks.contains(RankEnum.deratus.getRank())|| ranks.contains(RankEnum.legatus.getRank())) {
            playerJson.setPremium(false);
        }
        ContinentalManagers.tabManager.updatePlayer(this);
        refreshCommands();
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
        if (clientsides.remove(clientSide)) {
            clientSides.put(clientSideExtras, clientsides);
        } else {
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
        if (clientsides.isEmpty()) {
            clientSides.remove(clientSideExtras);
        } else {
            clientSides.put(clientSideExtras, clientsides);
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

    public Rank getDominantRank(){
        return ranks.peek();
    }

    public boolean isPremium(){
        return playerJson.isPremium();
    }

    public boolean isAutoVoteActive(){
        return playerJson.isAutoVoteActive();
    }

    public boolean isInOwnGame(){
        return isInOwnGame;
    }

    public void setInOwnGame(boolean b){
        this.isInOwnGame =b;
    }

    public Component getCPlayerName(){
        if (hasRank(RankEnum.default_rank.getRank())&&playerJson.isNicknameActive()){
            return MiniMessage.miniMessage().deserialize(playerJson.getNickname()).hoverEvent(HoverEvent.showText(Component.text()
                            .append(Component.text("Username: "+getUsername()))
                            .appendNewline()
                            .append(Component.text("Click to msg",NamedTextColor.GOLD))
                    .build())).clickEvent(ClickEvent.suggestCommand("/msg "+getUsername()+" "));
        }else{
            Rank rank = getDominantRank();
            return getName().color(rank.color).hoverEvent(HoverEvent.showText(Component.text("Click to msg",NamedTextColor.GOLD))).clickEvent(ClickEvent.suggestCommand("/msg "+getUsername()+" "));
        }
    }

    public Component getFullPrefix(){
        Rank rank = getDominantRank();
        Component prefix;
        Country c = getCountry();
        if (null == c) {
            prefix = Component.text("spectator", NamedTextColor.GRAY, TextDecoration.BOLD);
        } else {
            prefix = c.getPrefix();
        }
        return Component.text()
                .append(rank.prefix)
                .append(Component.text(" "))
                .append(prefix)
                .append(Component.text(" "))
                .append(getCPlayerName())
                .build();
    }

    public void setCountry(Country country){
        this.country=country;
        ContinentalManagers.tabManager.updatePlayer(this);
    }
}
