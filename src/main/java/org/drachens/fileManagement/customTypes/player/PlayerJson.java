package org.drachens.fileManagement.customTypes.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minestom.server.network.player.GameProfile;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.fileManagement.filetypes.GsonFileType;
import org.drachens.player_types.CPlayer;
import org.drachens.store.other.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.Messages.getTime;

public class PlayerJson extends GsonFileType {
    private final GameProfile gameProfile;
    private CPlayer p;
    private HashMap<String, Integer> eventAchievementTrigger;
    private Long playtime;
    private List<String> ranks;
    private CustomLoginRecord customLoginMessage;
    private boolean customLoginMessageActive = false;
    private boolean premium = false;
    private boolean autoVoteActive = false;
    private String autoVoteOption;
    private boolean suffixActive = false;
    private String suffix;

    public PlayerJson(String json, GameProfile gameProfile) {
        super(json);
        this.gameProfile=gameProfile;
        setDefaults();
    }

    @Override
    protected void initialLoad() {
        if (getConfig().getAsJsonObject("activity").has("playtime")){
            playtime = getConfig().getAsJsonObject("activity").get("playtime").getAsLong();
        }else {
            playtime = 0L;
        }
        p.setOriginalPlayTime(playtime);
        ranks = getFromList(String.class,"ranks");
        List<String> permissions = getFromList(String.class, "permissions");
        permissions.forEach(p::addPermission);
        eventAchievementTrigger = loadHashMap(Integer.class,"achievements","completed");
        set(new JsonPrimitive(getTime()),"activity","last-online");
        if (ranks.contains("legatus")||ranks.contains("deratus")){
            premiumSetDefaults();
            premiumLoad();
        }
    }

    protected void premiumSetDefaults(){
        premium=true;
        addDefault("","premium","login-message","join");
        addDefault("","premium","login-message","change-join");
        addDefault("","premium","login-message","change-leave");
        addDefault("","premium","login-message","leave");
        addDefault(new JsonPrimitive(false),"premium","login-message","active");
        addDefault(new JsonPrimitive(false),"premium","auto-vote","active");
        addDefault("","premium","auto-vote","current");
        addDefault("","premium","suffix","current");
        addDefault(new JsonPrimitive(false),"premium","suffix","active");
    }

    protected void premiumLoad(){
        JsonObject premium = getConfig().getAsJsonObject("premium");
        JsonObject loginMSG = premium.getAsJsonObject("login-message");
        String join = loginMSG.get("join").getAsString();
        String changeJoin = loginMSG.get("change-join").getAsString();
        String changeLeave = loginMSG.get("change-leave").getAsString();
        String leave = loginMSG.get("leave").getAsString();
        customLoginMessageActive = loginMSG.get("active").getAsBoolean();
        if (join != null && changeJoin != null && changeLeave != null && leave != null){
            customLoginMessage = new CustomLoginRecord(join,changeJoin,changeLeave,leave);
        }
        autoVoteOption = premium.getAsJsonObject("auto-vote").get("current").getAsString();
        autoVoteActive = premium.getAsJsonObject("auto-vote").get("active").getAsBoolean();
        suffixActive = premium.getAsJsonObject("suffix").get("active").getAsBoolean();
        suffix = premium.getAsJsonObject("suffix").get("current").getAsString();
    }

    public void laterInit(){
         new ArrayList<>(ranks).forEach(string ->{
            if (string.isBlank())return;
            Rank rank = RankEnum.valueOf(string).getRank();
            rank.addPlayer(p);
        });
    }

    @Override
    protected void setDefaults() {
        String time = getTime();
        addDefault(gameProfile.name(),"display", "username");
        addDefault(gameProfile.uuid().toString(),"display", "uuid");
        addDefault(0L,"activity","playtime");
        addDefault(time,"activity","last-online");
        addDefault(time,"activity","first-join");
        addDefault(new JsonArray(),"achievements","completed");
        addDefault(new JsonArray(),"permissions");
        addDefault(new JsonArray(),"ranks");
    }

    public void setPlaytime(Long playtime) {
        this.playtime = playtime;
        set(playtime,"activity","playtime");
    }

    public void addRank(String toAdd) {
        ranks.add(toAdd);
        addToList(toAdd,String.class,"ranks");
    }

    public void removeRank(String toAdd) {
        ranks.remove(toAdd);
        removeFromList(toAdd,String.class,"ranks");
    }

    public HashMap<String, Integer> getEventAchievementTrigger() {
        return eventAchievementTrigger;
    }

    public void addAchievementEventTriggered(String identifier, int count) {
        eventAchievementTrigger.put(identifier, count);
        saveHashMap(eventAchievementTrigger, Integer.class,"achievements","completed");
    }

    public void setCustomLoginMessage(CustomLoginRecord clr){
        this.customLoginMessage=clr;
        set(clr.join(),"premium","login-message","join");
        set(clr.changeJoin(),"premium","login-message","change-join");
        set(clr.changeLeave(),"premium","login-message","change-leave");
        set(clr.leave(),"premium","login-message","leave");
    }

    public void setLoginMessageActive(boolean b){
        customLoginMessageActive = b;
        set(new JsonPrimitive(b),"premium","login-message","active");
    }

    public boolean isCustomLoginMessageActive(){
        return customLoginMessageActive;
    }

    public CustomLoginRecord getCustomLoginMessage(){
        return customLoginMessage;
    }

    public boolean isPremium(){
        return premium;
    }

    public void setPremium(boolean b){
        premium=b;
    }
    
    public boolean isAutoVoteActive(){
        return autoVoteActive;
    }

    public void setAutoVoteActive(boolean autoVoteActive) {
        this.autoVoteActive = autoVoteActive;
        set(new JsonPrimitive(autoVoteActive),"premium","auto-vote","active");
    }

    public String getAutoVoteOption(){
        return autoVoteOption;
    }

    public void setAutoVoteOption(String autoVoteOption) {
        this.autoVoteOption = autoVoteOption;
        set(autoVoteOption,"premium","auto-vote","current");
    }

    public void setPlayer(CPlayer player){
        this.p=player;
        p.setPlayerJson(this);
        initialLoad();
    }

    public boolean isSuffixActive(){
        return suffixActive;
    }

    public void setSuffixActive(boolean b){
        this.suffixActive=b;
        set(new JsonPrimitive(b),"premium","suffix","active");
    }

    public String getSuffix(){
        return suffix;
    }

    public void setSuffix(String suffix){
        this.suffix=suffix;
        set(suffix,"premium","suffix","current");
    }
}
