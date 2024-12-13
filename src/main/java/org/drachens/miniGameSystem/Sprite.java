package org.drachens.miniGameSystem;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Task;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sprite {
    private final Monitor monitor;
    private final Map<RelativePos, DynamicPixel> materialHashMap;
    private Pos pos;
    private Task task;

    public Sprite(Pos pos, Monitor monitor) {
        this.pos = pos;
        this.materialHashMap = new HashMap<>();
        this.monitor = monitor;
    }

    public Sprite addDynamicPixel(RelativePos relativePos, DynamicPixel dynamicPixel) {
        materialHashMap.put(relativePos, dynamicPixel);
        double x = pos.x() + relativePos.getX();
        double y = pos.y() + relativePos.getY();
        monitor.addDynamicPixel(new Pos(x, y, 0), dynamicPixel);
        return this;
    }

    public void setPos(Pos newPos) {
        for (Map.Entry<RelativePos, DynamicPixel> entry : materialHashMap.entrySet()) {
            RelativePos relativePos = entry.getKey();
            DynamicPixel dynamicPixel = entry.getValue();

            double prevX = this.pos.x() + relativePos.getX();
            double prevY = this.pos.y() + relativePos.getY();
            double newX = newPos.x() + relativePos.getX();
            double newY = newPos.y() + relativePos.getY();

            monitor.moveDynamicPixel(new Pos(prevX, prevY, 0), new Pos(newX, newY, 0), dynamicPixel);
        }
        this.pos = newPos;
    }

    public void move(Pos to, Long delayInMillis) {
        if (task != null) {
            task.cancel();
        }

        List<Pos> posList = monitor.getAStarPathfinder().findPath(pos, to, monitor.getInstance());
        if (posList.isEmpty()) return;

        task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (posList.isEmpty()) {
                task.cancel();
                return;
            }
            Pos nextPos = posList.removeFirst();
            setPos(nextPos);
        }).repeat(delayInMillis, ChronoUnit.MILLIS).schedule();
    }

    public Pos getPos() {
        return pos;
    }
}
