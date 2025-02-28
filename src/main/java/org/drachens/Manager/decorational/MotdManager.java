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
            responseData.setFavicon("data:image/png;base64,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");
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
