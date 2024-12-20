package org.drachens.miniGameSystem;

import dev.ng5m.util.Preconditions;
import dev.ng5m.util.VoidFunction;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sprite {
    private final Monitor monitor;
    private final Map<RelativePos, DynamicPixel> materialHashMap;
    private final String identifier;
    private Pos pos;
    private final MiniGameRunnable miniGameRunnable;

    public Sprite(Pos pos, Monitor monitor, String identifier, MiniGameRunnable miniGameRunnable) {
        this.pos = pos;
        this.materialHashMap = new HashMap<>();
        this.monitor = monitor;
        this.identifier = identifier;
        this.miniGameRunnable = miniGameRunnable;
    }

    public Sprite(Pos pos, Monitor monitor, String identifier, VoidFunction<Sprite> miniGameRunnable) {
        this(pos, monitor, identifier, (collided, pos1) -> miniGameRunnable.apply(collided));
    }

    public DynamicPixel getDynamicPixel(Pos pos){
        return materialHashMap.get(new RelativePos((int) (pos.x() - this.pos.x()), (int) (pos.y() - this.pos.y())));
    }

    public void addDynamicPixel(RelativePos relativePos, DynamicPixel dynamicPixel) {
        materialHashMap.put(relativePos, dynamicPixel);
        dynamicPixel.setSprite(this);
        monitor.addDynamicPixel(new Pos(pos.x() + relativePos.x(), pos.y() + relativePos.y(), 0), dynamicPixel);
    }

    public void setPos(Pos newPos) {
        for (Map.Entry<RelativePos, DynamicPixel> entry : materialHashMap.entrySet()) {
            RelativePos relativePos = entry.getKey();
            DynamicPixel dynamicPixel = entry.getValue();

            double prevX = this.pos.x() + relativePos.x();
            double prevY = this.pos.y() + relativePos.y();
            double newX = newPos.x() + relativePos.x();
            double newY = newPos.y() + relativePos.y();

            monitor.moveDynamicPixel(new Pos(prevX, prevY, 0), new Pos(newX, newY, 0), dynamicPixel);
        }
        this.pos = newPos;
    }

    public void delete() {
        materialHashMap.forEach((relativePos, dynamicPixel) -> monitor.removeDynamicPixel(new Pos(pos.x() + relativePos.x(), pos.y() + relativePos.y(), 0), dynamicPixel));
    }

    public Pos getPos() {
        return pos;
    }

    public String getIdentifier() {
        return identifier;
    }


    public void onCollision(Sprite collided, Pos pos) {
        if (miniGameRunnable == null) return;
        miniGameRunnable.run(collided,pos);
    }

    public static class Builder {
        private String s;
        private int weight = 0;
        private String identifier;
        private MiniGameRunnable collisionFunction;
        private final HashMap<Character, Material> ingredients = new HashMap<>();

        public Builder setIngredient(char c, Material m) {
            ingredients.put(c, m);
            return this;
        }

        public Builder setLayout(String layout) {
            this.s = layout;
            return this;
        }

        public Builder setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder setCollisionFunction(MiniGameRunnable collisionFunction) {
            this.collisionFunction = collisionFunction;
            return this;
        }

        public Builder setCollisionFunction(VoidFunction<Sprite> function) {
            return setCollisionFunction((collided, pos) -> function.apply(collided));
        }

        public Builder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public static void loadLayout(int weight, String layout, Map<Character, Material> ingredients, Sprite sprite) {
            var spl = layout.split("\n");

            for (int y = 0; y < spl.length; ++y) {
                for (int x = 0; x < spl[y].length(); ++x) {
                    char c = spl[y].charAt(x);

                    if (!ingredients.containsKey(c)) continue;

                    sprite.addDynamicPixel(new RelativePos(-x, -y), new DynamicPixel(weight, ingredients.get(c)));
                }
            }
        }

        public Sprite build(Pos pos, Monitor monitor) {
            Preconditions.assertNotNull(s, "Layout cannot be null");

            Sprite sprite = new Sprite(pos, monitor, identifier, collisionFunction);

            loadLayout(weight, s, ingredients, sprite);

            return sprite;
        }

    }
}