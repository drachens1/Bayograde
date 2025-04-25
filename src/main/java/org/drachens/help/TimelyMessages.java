package org.drachens.help;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.drachens.util.Messages.globalBroadcast;

public class TimelyMessages {
    public TimelyMessages(List<Component> timelyMessageList){
        MinecraftServer.getSchedulerManager().buildTask(()->{
            globalBroadcast(timelyMessageList.get(new Random().nextInt(timelyMessageList.size())));
        }).repeat(5,ChronoUnit.MINUTES).schedule();
    }
    public static class Builder {
        private final List<Component> timelyMessageList = new ArrayList<>();
        public Builder addMessage(Component component){
            timelyMessageList.add(component);
            return this;
        }
        public TimelyMessages build(){
            return new TimelyMessages(timelyMessageList);
        }
    }
}
