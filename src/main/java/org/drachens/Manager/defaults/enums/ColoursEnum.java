package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.format.TextColor;

public enum ColoursEnum {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    YELLOW(255, 255, 0),
    CYAN(0, 255, 255),
    MAGENTA(255, 0, 255),
    ORANGE(255, 165, 0),
    PURPLE(128, 0, 128),
    PINK(255, 192, 203),
    BLACK(0, 0, 0),
    WHITE(255, 255, 255),
    GRAY(128, 128, 128),
    LIGHT_GRAY(211, 211, 211),
    DARK_GRAY(64, 64, 64),
    BROWN(139, 69, 19),
    GOLD(255, 215, 0),
    SILVER(192, 192, 192),
    MAROON(128, 0, 0),
    NAVY(0, 0, 128),
    TEAL(0, 128, 128);

    private final TextColor textColor;
    ColoursEnum(int r, int g, int b){
        this.textColor=TextColor.color(r,g,b);
    }
    public TextColor getTextColor(){
        return textColor;
    }
}
