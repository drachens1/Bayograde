package org.drachens.store.other;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class LoginMessage {
    private final Function<CPlayer, Component> playerJoin;
    private final Function<CPlayer, Component> playerChangeInstanceTo;
    private final Function<CPlayer, Component> playerChangeInstanceFrom;
    private final Function<CPlayer, Component> playerLeave;
    public LoginMessage(Function<CPlayer, Component> playerJoin, Function<CPlayer, Component> playerChangeInstanceTo, Function<CPlayer, Component> playerChangeInstanceFrom, Function<CPlayer, Component> playerLeave){
        this.playerJoin=playerJoin;
        this.playerChangeInstanceTo=playerChangeInstanceTo;
        this.playerChangeInstanceFrom=playerChangeInstanceFrom;
        this.playerLeave=playerLeave;
    }
    public Component onPlayerJoin(CPlayer p){
        return playerJoin.apply(p);
    }

    public Component onPlayerChangeInstanceTo(CPlayer p){
        return playerChangeInstanceTo.apply(p);
    }

    public Component onPlayerChangeInstanceFrom(CPlayer p){
        return playerChangeInstanceFrom.apply(p);
    }

    public Component onPlayerLeave(CPlayer p){
        return playerLeave.apply(p);
    }
}
