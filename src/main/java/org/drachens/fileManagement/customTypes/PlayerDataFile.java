package org.drachens.fileManagement.customTypes;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.fileManagement.YamlFileType;
import org.spongepowered.configurate.serialize.SerializationException;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.drachens.util.Messages.getTime;

public class PlayerDataFile extends YamlFileType {
    private final Player p;

    public PlayerDataFile(Player p) {
        super(p.getUuid().toString(), "playerData/" + p.getUuid() + ".yml");
        this.p = p;
        setDefaults();
        initialLoad();
    }

    @Override
    protected void initialLoad() {
        List<String> perms = getPermissions();

        if (perms.contains("operator"))
            ContinentalManagers.permissions.playerOp(p);

        CPlayer cPlayer = (CPlayer) p;

        cPlayer.setPlayerDataFile(this);
        cPlayer.setGold(getGold());
        cPlayer.setJoinTime(LocalTime.now());
        cPlayer.setHead();
        cPlayer.setPlayTime(getPlaytime());

        getCosmetics().forEach(cPlayer::addCosmetic);
        System.out.println(getCosmetics()+" Cosmetics");
        System.out.println(cPlayer.getOwnedCosmetics() +" owned cosmetics");
    }

    @Override
    protected void setDefaults() {
        try {
            String time = getTime();
            String date = new SimpleDateFormat("dd/MM/yy").format(new Date());

            addDefault(p.getUuid().toString(),"Player_Info", "Identifiers", "UUID");
            addDefault(p.getUsername(),"Player_Info","Identifiers","Username");
            set(time,"Player_Info", "Activity", "LastOnline");
            addDefault(time,"Player_Info","Activity","FirstJoined");
            addDefault(date,"Player_Info", "Activity", "DailyPlaytime");
            addDefault(0L,"Player_Info", "Activity", "TotalPlayTime");
            addDefault(0,"Currencies","Gold");
            addDefault("","Permissions");
            addDefault("","Cosmetics");
            addDefault("","Achievements");
            save();
        } catch (SerializationException e) {
            System.err.println("Error setting defaults "+e.getMessage());
        }
    }

    public long getPlaytime(){
        return getConfigurationNode().node("Player_Info", "Activity", "TotalPlayTime").getLong();
    }

    public void setPlaytime(Long playtime){
        set(playtime,"Player_Info", "Activity", "TotalPlayTime");
        save();
    }

    public int getGold(){
        return getConfigurationNode().node("Currencies","Gold").getInt();
    }

    public void setGold(int gold){
        set(gold,"Currencies","Gold");
        save();
    }

    public List<String> getPermissions(){
        return stripEmpty(getFromList(String.class,"Permissions"));
    }

    public void addPermission(String permission){
        addToList(permission, String.class,"Permissions");
    }

    public List<String> getCosmetics(){
        return stripEmpty(getFromList(String.class, "Cosmetics"));
    }

    public void addCosmetic(String cosmetic){
        addToList(cosmetic, String.class,"Cosmetics");
    }

    public void removeCosmetic(String cosmetic){
        addToList(cosmetic, String.class,"Cosmetics");
    }
}
