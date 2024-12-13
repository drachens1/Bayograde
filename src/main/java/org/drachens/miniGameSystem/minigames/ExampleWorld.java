package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.InstanceContainer;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.Sprite;

import java.time.temporal.ChronoUnit;

public class ExampleWorld extends World {
    private Example miniGame;

    public ExampleWorld() {
        super(MinecraftServer.getInstanceManager().createInstanceContainer());

    }

    public void setMiniGame(MiniGame miniGame){
        this.miniGame= (Example) miniGame;
    }

    @Override
    public void addPlayer(CPlayer p) {

        MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            Pos last = p.getPosition();
            @Override
            public void run() {
                Sprite sprite = miniGame.getSprite();
                Pos change = p.getPosition();
                int x = (int) (last.x()-change.x());
                int y = (int) (last.y()-change.y());
                Pos pos1 = new Pos(sprite.getPos().x()-x,sprite.getPos().y()-y,0);
                last=p.getPosition();
                System.out.println(pos1);
                sprite.move(pos1,0L);
            }
        }).repeat(300L, ChronoUnit.MILLIS).schedule();
    }

    @Override
    public void removePlayer(CPlayer p) {

    }
    @Override
    public void playerMove(PlayerMoveEvent e) {

    }

    @Override
    public void playerBlockInteract(PlayerBlockInteractEvent e) {

    }

    @Override
    public void playerUseItem(PlayerUseItemEvent e) {

    }

    @Override
    public void playerStartDigging(PlayerStartDiggingEvent e) {

    }

    @Override
    public void playerDisconnect(PlayerDisconnectEvent e) {

    }
}
