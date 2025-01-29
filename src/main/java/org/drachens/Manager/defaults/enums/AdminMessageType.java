package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;

public enum AdminMessageType {
    swears(Component.text("|SWEARS| "));

    public final Component prefix;
    AdminMessageType(Component prefix){
        this.prefix=prefix;
    }
}
