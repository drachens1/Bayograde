package dev.ng5m;

import dev.ng5m.bansystem.BanManager;
import net.kyori.adventure.text.format.TextColor;

import java.io.File;

public class Constants {
    public static final BanManager BAN_MANAGER = new BanManager(new File("bans.json"));

    public enum Colors {
        LIGHT_GRAY(TextColor.color(135, 135, 135)),
        LIME(TextColor.color(0, 255, 0)),
        RED(TextColor.color(255, 0, 72));

        public final TextColor color;

        Colors(TextColor color) {
            this.color = color;
        }
    }

}
