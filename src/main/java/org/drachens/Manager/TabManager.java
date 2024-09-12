package org.drachens.Manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.minestom.server.MinecraftServer;
import org.intellij.lang.annotations.RegExp;

import java.time.temporal.ChronoUnit;

public class TabManager {
    public TabManager(){
        MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            @Override
            public void run() {

            }
        }).repeat(500, ChronoUnit.MILLIS).schedule();
    }

    private Component replaceTxt(Component component, @RegExp String from, String to){
        return component.replaceText(TextReplacementConfig.builder()
                .match(from)
                .replacement(to)
                .build());
    }
}
