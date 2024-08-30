package org.drachens.dataClasses;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Ban {
    private final UUID player;
    private final Date end;
    private final String reason;
    public Ban(UUID player, Date end, String reason){
        this.end=end;
        this.player=player;
        this.reason = reason;
    }
    public UUID getPlayer(){
        return player;
    }
    public Date getEnd(){
        return end;
    }
    public String getReason(){
        return reason;
    }
}
