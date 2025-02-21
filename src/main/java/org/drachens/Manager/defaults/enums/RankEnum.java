package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.drachens.store.other.Rank;

@Getter
public enum RankEnum {
    default_rank(new Rank.Create(Player::getName, Component.text(""),Component.text(""), NamedTextColor.GRAY,"default_rank",0)
            .build()),
    legatus(new Rank.Create(Player::getName, Component.text("\uD83D\uDE3E"),Component.text(""),NamedTextColor.GRAY,"legatus",1)
            .build()),
    deratus(new Rank.Create(Player::getName, Component.text("\uD83D\uDC1D"),Component.text(""),NamedTextColor.GRAY,"deratus",2)
            .build())
    ;

    private final Rank rank;
    RankEnum(Rank rank){
        this.rank=rank;
    }
}