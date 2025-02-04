package org.drachens.fileManagement.customTypes;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.fileManagement.filetypes.GsonFileType;
import org.drachens.player_types.CPlayer;
import org.drachens.store.other.Rank;

import java.util.HashMap;
import java.util.List;

import static org.drachens.util.Messages.getTime;

public class PlayerJson extends GsonFileType {
    private final CPlayer p;
    private HashMap<String, Integer> eventAchievementTrigger;
    private Long playtime;
    private List<String> ranks;

    public PlayerJson(String json, CPlayer p) {
        super(json);
        this.p=p;
        p.setPlayerJson(this);
        setDefaults();
        initialLoad();
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
        ranks.forEach(string ->{
            if (string.isBlank())return;
            Rank rank = RankEnum.valueOf(string).getRank();
            rank.addPlayer(p);
        });
        List<String> permissions = getFromList(String.class, "permissions");
        permissions.forEach(p::addPermission);
        eventAchievementTrigger = loadHashMap(Integer.class,"achievements","completed");
        set(new JsonPrimitive(getTime()),"activity","last-online");
    }

    @Override
    protected void setDefaults() {
        String time = getTime();
        addDefault(p.getUsername(),"display", "username");
        addDefault(p.getUuid().toString(),"display", "uuid");
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
}
