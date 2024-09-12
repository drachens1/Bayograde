package org.drachens.Manager;

import org.drachens.dataClasses.Ban;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.drachens.cmd.Dev.whitelist.Whitelist;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.drachens.util.PermissionsUtil.playerOp;
import static org.drachens.util.PlayerUtil.getPlayerFromUUID;
import static org.drachens.util.ServerUtil.start;

public class ConfigFileManager {
    private static ConfigurationNode banListNode;
    private static YamlConfigurationLoader banLoader;    // contains all the banned peoples as UUID's
    private static final List<UUID> banList = new ArrayList<>();
    private static final List<Ban> bans = new ArrayList<>();
    private static ConfigurationNode whitelistListNode;
    private static YamlConfigurationLoader whitelistLoader;    // contains all the whitelisted peoples as UUID's
    private static ConfigurationNode permissionsNode;
    private static YamlConfigurationLoader permissionsLoader;  // Has the permission groups that are created
    private static final HashMap<String,ConfigurationNode> playerDataNodes = new HashMap<>(); // UUID as a string for the name
    private static final HashMap<String,YamlConfigurationLoader> playerDataLoaders = new HashMap<>(); // contains all the players perms as UUID's for the name of the files
    private static final HashMap<String,File> playerDataFiles = new HashMap<>();
    private static String logMsg;
    private static String logCmds;
    private static File msg;
    private static File cmd;
    private static Whitelist whitelist;
    private static List<Process> processes = new ArrayList<>();
    public static void startup(){
        //Gets the file and loads it using YAML
        File banFile = new File("bans.yml");
        fileExists(banFile);
        banLoader = YamlConfigurationLoader.builder()
                .file(banFile)
                .build();

        //Loads it into a configuration node
        try {
            banListNode = banLoader.load();
        } catch (IOException e) {
            System.err.println("Unable to load bans loader "+e.getMessage());
        }

        File playerData = new File("playerData");//Creates the parent directory
        playerData.mkdir();

        for (File f : Objects.requireNonNull(playerData.listFiles())){
            playerDataFiles.put(f.getName(),f);
            playerDataLoaders.put(f.getName(),YamlConfigurationLoader.builder()
                    .file(f)
                    .build());
        }

        for (Map.Entry<String,YamlConfigurationLoader> e : playerDataLoaders.entrySet()){
            try {
                playerDataNodes.put(playerDataFiles.get(e.getKey()).getName(),e.getValue().load());
            } catch (IOException err) {
                System.err.println("Unable to load operator loader "+err.getMessage());
            }
        }

        //To setup all the log stuff
        System.out.println("Creating logs");
        new File("logs").mkdir();
        new File("logs/cmds").mkdir();
        new File("logs/msg").mkdir();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy.HH.mm");
        logCmds = "logs/cmds/"+format.format(new Date())+".txt";
        cmd = new File(logCmds);
        fileExists(cmd);
        logMsg = "logs/msg/"+format.format(new Date())+".txt";
        msg = new File(logMsg);
        fileExists(msg);
        System.out.println("Logs finished");

        //Permissions
        File permissions = new File("permissions.yml");
        fileExists(permissions);
        permissionsLoader = YamlConfigurationLoader.builder()
                .file(permissions)
                .build();
        try {
            permissionsNode = permissionsLoader.load();
        } catch (ConfigurateException e) {
            System.err.println("Unable to load permissions "+e.getMessage());
        }
        loadBannedPeople();
        loadWhitelist();
        start();
    }
    private static void loadBannedPeople(){// Loads all the banned people
        System.out.println("Banned list loading....");
        ConfigurationNode bansList = banListNode.node("Bans");
        StringBuilder bannedPlayers = new StringBuilder();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : bansList.childrenMap().entrySet()) {
            String uuidString = entry.getKey().toString();

            UUID player = UUID.fromString(uuidString);
            ConfigurationNode banned = entry.getValue();

            float duration = banned.node("duration").getFloat();
            String reason = banned.node("reason").getString();

            banList.add(player);

            Date currentDate = new Date();
            long durationMillis = TimeUnit.MINUTES.toMillis((long) duration);
            Date endDate = new Date(currentDate.getTime() + durationMillis);

            bans.add(new Ban(player, endDate, reason));
            bannedPlayers.append(player).append(",");
        }

        if (!bannedPlayers.isEmpty()) {
            bannedPlayers.setLength(bannedPlayers.length() - 1);
        }

        System.out.println("Banned Players: " + bannedPlayers);
    }
    private static void loadWhitelist(){
        System.out.println("Whitelist started loading...");
        File whitelistFile = new File("whitelist.yml");
        fileExists(whitelistFile);
        whitelistLoader = YamlConfigurationLoader.builder()
                .file(whitelistFile)
                .build();
        try {
            whitelistListNode = whitelistLoader.load();
        } catch (ConfigurateException e) {
            System.out.println("Whitelist failed loading "+e.getMessage());
        }
        ConfigurationNode whitelisted = whitelistListNode.node("whitelist");
        ConfigurationNode actives = whitelisted.node("active");
        ConfigurationNode player = whitelisted.node("players");

        boolean active =  actives.getBoolean();
        try {
            List<String> players = new ArrayList<>(Objects.requireNonNull(player.getList(String.class)));
            List<UUID> playerID = new ArrayList<>();
            for (String s : players){
                playerID.add(UUID.fromString(s));
            }
            whitelist = new Whitelist(playerID,active);
            return;
        } catch (SerializationException e) {
            System.out.println("Players loading error "+e.getMessage());
        }
        whitelist = new Whitelist(new ArrayList<>(),false);
    }
    public static boolean isBanned(UUID p){
        return banList.contains(p);
    }
    public static Component getBanMSG(Player p){
        Ban b = bans.get(banList.indexOf(p.getUuid()));//Contains all the info to display the ban reason
        return Component.text()
                .append(Component.text(p.getUsername(), NamedTextColor.GOLD))
                .append(Component.text("Banned Reason:"))
                .append(Component.text(b.getReason()))
                .append(Component.text(" Ends in "))
                .append(Component.text(b.getEnd().toString()))
                .build();
    }
    public static ConfigurationNode getPlayersData(UUID player){//The string should be a UUID
        String playerName = player.toString()+".yml";
        if (!playerDataNodes.containsKey(playerName)){
            return createPlayersData(playerName);
        }
        return playerDataNodes.get(playerName);
    }
    public static YamlConfigurationLoader getPlayerDataLoader(UUID player){
        String playerName = player.toString()+".yml";
        if (!playerDataLoaders.containsKey(playerName)){
            createPlayersData(playerName);
            return playerDataLoaders.get(playerName);
        }
        return playerDataLoaders.get(playerName);
    }
    public static ConfigurationNode createPlayersData(String player){
        File playerData = new File("playerData/"+player+".yml");
        fileExists(playerData);
        System.out.println("creating player data file! "+player+" Name: "+playerData.getName());
        YamlConfigurationLoader temp = YamlConfigurationLoader.builder()
                .file(playerData)
                .build();
        playerDataLoaders.put(playerData.getName(),temp);
        playerDataFiles.put(playerData.getName(),playerData);

        try {
            ConfigurationNode c = temp.load();
            playerDataNodes.put(playerData.getName(),c);
            //Creates the default info
            ConfigurationNode general = c.node("general");
            general.node("name").set(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(UUID.fromString(player)));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
            general.node("joined").set(format.format(new Date()));
            general.node("lastOnline").set(format.format(new Date()));
            playerDataLoaders.get(playerData.getName()).save(playerDataNodes.get(playerData.getName()));
            return c;
        } catch (IOException e) {
            System.err.println("Unable to create the player datas "+e.getMessage());
            return null;
        }
    }
    private static void fileExists(File f){
        if (!f.exists()) {
            try{
                f.createNewFile();
                System.out.println(f.getName()+" was created");
            } catch (IOException e){
                //don't need anything here
            }
        }
    }
    public static void playerSave(UUID player){
        String playerName = player.toString();
        if (!playerDataNodes.containsKey(playerName)){
            return;
        }
        try{
            playerDataLoaders.get(playerName).save(playerDataNodes.get(playerName));
        } catch (ConfigurateException e){
            System.err.println("Unable to save player data of player "+player+" error msg : " + e.getMessage());
        }
    }
    public static String getLogMsg(){
        return logMsg;
    }
    public static String getLogCmds(){
        return logCmds;
    }
    public static void addBan(UUID p, String[] reason, float duration){
        banList.add(p);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");

        long durations = TimeUnit.MINUTES.toMillis((long) duration);

        Date newDate = new Date(new Date().getTime() + durations);
        StringBuilder a = new StringBuilder();
        for (String s : reason){
            a.append(s).append(" ");
        }
        String reason2 = a.toString();//adds spaces to the reason so it looks correct
        bans.add(new Ban(p,newDate,reason2));
        try {
            ConfigurationNode bansNode = banListNode.node("Bans");

            String banDate = format.format(new Date());

            ConfigurationNode playerNode = bansNode.node(p.toString());

            playerNode.node("banDate").set(banDate);
            playerNode.node("duration").set(duration);
            playerNode.node("reason").set(reason2);

            banLoader.save(banListNode);

            Player pl = getPlayerFromUUID(p);
            if (pl != null){
                pl.kick(getBanMSG(pl));
            }
        } catch (ConfigurateException e){
            System.err.println("Add bans error "+e.getMessage());
        }
    }
    public static void unBan(UUID p) {
        int index = banList.indexOf(p);
        if (index != -1) {
            bans.remove(index);
            banList.remove(p);
        }
        ConfigurationNode bansList = banListNode.node("Bans");
        if (bansList.removeChild(p.toString())){
            System.out.println("Unban successful");
        }
        try {
            banLoader.save(banListNode);
        } catch (ConfigurateException e) {
            System.err.println("Failed to unban player " + p + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static void loadPermissions(Player p){
        UUID playerID = p.getUuid();
        ConfigurationNode wanted = getPlayersData(playerID);
        ConfigurationNode permissions = wanted.node("permissions");
        try {
            List<String> perms = permissions.getList(String.class);
            if (perms == null) {
                perms = new ArrayList<>();
            }

            if (perms.contains("operator")) {
                playerOp(p);
            }
        }catch (SerializationException e) {
            System.out.println("Player operator failed "+p.getUsername()+" "+e.getMessage());
        }
    }
    public static void shutdown(){
        Scanner cmds = new Scanner(logCmds);
        Scanner log = new Scanner(logMsg);
        if (!cmds.hasNextLine())cmd.delete();
        if (!log.hasNextLine())msg.delete();
        for (Process pb : processes){
            if (pb.isAlive()){
                pb.destroy();
            }
        }
    }
    public static Whitelist getWhitelist(){
        return whitelist;
    }
    public static ConfigurationNode getWhitelistListNode(){
        return whitelistListNode;
    }
    public static void specificSave(String choice){
        System.out.println("Specifically saving: "+choice);
        switch (choice){
            case "whitelist":
                try {
                    whitelistLoader.save(whitelistListNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving whitelist: "+e.getMessage());
                }
                break;
            case "ban":
                try {
                    banLoader.save(banListNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving ban: "+e.getMessage());
                }
            case "permissions":
                try {
                    permissionsLoader.save(permissionsNode);
                } catch (ConfigurateException e) {
                    System.err.println("Error saving permissions: "+e.getMessage());
                }
        }
    }
    public static ConfigurationNode getPermissionsFile(){
        return permissionsNode;
    }
}
