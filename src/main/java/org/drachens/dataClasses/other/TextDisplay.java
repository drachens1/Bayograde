package org.drachens.dataClasses.other;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Provinces.Province;

import java.util.List;

public class TextDisplay extends Entity {
    private Component text;

    public TextDisplay(Component text, Pos pos, Instance instance) {
        this(text, pos, instance, false);
    }

    public TextDisplay(Component text, Pos pos, Instance instance, boolean invisible) {
        super(EntityType.TEXT_DISPLAY);
        this.text = text;
        setInstance(instance, pos);
        initializeDisplay();
        this.setInvisible(invisible);
    }

    public TextDisplay(Component text, Pos pos, Instance instance, List<Player> viewers) {
        this(text, pos, instance, true);
        for (Player p : viewers) {
            this.addViewer(p);
        }
    }

    public TextDisplay(Component text, Province province) {
        this(text, province.getPos(), province.getInstance(), false);
    }

    public TextDisplay(Component text, Province province, boolean invisible) {
        this(text, province.getPos(), province.getInstance(), invisible);
    }

    public TextDisplay(Component text, Province province, List<Player> viewers) {
        this(text, province.getPos(), province.getInstance(), true);
        for (Player p : viewers) {
            this.addViewer(p);
        }
    }

    private void initializeDisplay() {
        this.setCustomNameVisible(false);
        this.setNoGravity(true);
        if (this.getEntityMeta() instanceof TextDisplayMeta textDisplayMeta) {
            textDisplayMeta.setText(text);

        }
    }

    public void delete() {
        this.remove();
    }

    public void move(Pos pos) {
        this.teleport(pos);
    }

    public void move(Province province) {
        this.teleport(province.getPos());
    }

    public void setText(Component text) {
        this.text = text;
        if (this.getEntityMeta() instanceof TextDisplayMeta textDisplayMeta) {
            textDisplayMeta.setText(text);
        }
    }

    public void hide(Player p) {
        this.removeViewer(p);
    }

    public void show(Player p) {
        this.addViewer(p);
    }

    public void setVisible(Boolean visible) {  // Renamed for clarity
        this.setInvisible(!visible);
    }
}
