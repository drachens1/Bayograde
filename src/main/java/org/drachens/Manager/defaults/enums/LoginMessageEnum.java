package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.store.other.LoginMessage;

public enum LoginMessageEnum {
    default_login_message(new LoginMessage(
            player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("+", NamedTextColor.GREEN))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build(), player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("-", NamedTextColor.RED))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build(), player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("+3", NamedTextColor.GREEN))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build(),
            player -> Component.text()
            .append(Component.text(player.getUsername(), NamedTextColor.GRAY))
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("-", NamedTextColor.GREEN))
            .append(Component.text("]", NamedTextColor.GRAY))
            .build()
    ));
    private final LoginMessage loginMessage;
    LoginMessageEnum(LoginMessage loginMessage){
        this.loginMessage=loginMessage;
    }
    public LoginMessage getLoginMessage(){
        return loginMessage;
    }
}
