package org.drachens.dataClasses.customgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class InterchangeWorld extends World {
    private final CPlayer p;
    private final List<CPlayer> players = new ArrayList<>();
    private final Component welcomeMessage;

    public InterchangeWorld(CPlayer p){
        super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(0, 1, 0));
        fill(new Pos(10,0,10),new Pos(-10,0,-10),Block.BLACK_CONCRETE);
        ContinentalManagers.worldManager.registerWorld(this);
        welcomeMessage=Component.text()
                .append(Component.text("| Welcome to a custom game.\n| These are basically the same as the global one but\n| the owner: "+p.getUsername()+" can activate certain DLC's \n| Also there is no voting period", NamedTextColor.GREEN))
                .build();
        this.p=p;
        p.sendMessage(Component.text()
                        .append(Component.text("| Welcome to a custom game.\n| You can activate any DLC's if you have any\n| Commands:\n| /manage creation complete #Completes the waiting period\n| /manage creation cancel #Cancels the waiting period\n| /manage invite <player> #Invites a player\n| /manage kick <player> #Kicks a player\n| /manage options #Shows the options/clickable options for commands to make it easier\n| That's all! have fun!",NamedTextColor.GREEN))
                .build());
        p.setInstance(getInstance());
        p.setLeaderOfOwnGame(true);
    }

    public void complete(){
        players.forEach(player -> {
            player.setInInterchange(false);
            player.setInOwnGame(true);
        });
    }

    public void delete(){

    }

    @Override
    public void addPlayer(CPlayer p) {
        players.add(p);
        if (!p.isLeaderOfOwnGame())p.sendMessage(welcomeMessage);
        p.setInInterchange(true);
        p.refreshCommands();
    }

    @Override
    public void removePlayer(CPlayer p) {
        players.remove(p);
        p.setInInterchange(false);
        p.setLeaderOfOwnGame(false);
        p.refreshCommands();
    }
}
