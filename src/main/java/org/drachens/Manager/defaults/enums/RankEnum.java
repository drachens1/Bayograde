package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.drachens.store.other.Rank;

public enum RankEnum {
    default_rank(new Rank.Create(Player::getName, Component.text(""),Component.text(""), NamedTextColor.GRAY,"default_rank")
            .addLoginMessage(LoginMessageEnum.default_login_message)
            .build());

    private final Rank rank;
    RankEnum(Rank rank){
        this.rank=rank;
    }
    public Rank getRank(){
        return rank;
    }
}
