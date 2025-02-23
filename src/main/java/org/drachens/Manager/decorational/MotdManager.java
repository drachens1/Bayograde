package org.drachens.Manager.decorational;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;
import org.drachens.Manager.defaults.enums.ColoursEnum;

public class MotdManager {
    public MotdManager(){
        MinecraftServer.getGlobalEventHandler().addListener(ServerListPingEvent.class, e->{
            ResponseData responseData = new ResponseData();
            responseData.setDescription(Component.text()
                    .append(Component.text("            BAYOGRADE ", ColoursEnum.LIME.getTextColor(),TextDecoration.BOLD))
                    .append(Component.text("    RELEASE #1"))
                    .appendNewline()
                    .append(Component.text("           WAR GAMES", ColoursEnum.RED.getTextColor()))
                    .append(Component.text(" | "))
                    .append(Component.text("SAVE", ColoursEnum.GREEN.getTextColor()))
                    .append(Component.text(" | "))
                    .append(Component.text("CO-OP", ColoursEnum.YELLOW.getTextColor()))
                    .append(Component.text(" | "))
                    .append(Component.text("TROOPS ", ColoursEnum.PURPLE.getTextColor()))
                    .build());
            responseData.setMaxPlayer(512);
           e.setResponseData(responseData);
        });
    }
}
