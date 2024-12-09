package org.drachens.dataClasses.Economics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;

public class Stability {
    private final Country country;
    private float stability;
    private final Modifier stabilityModifier;
    public Stability(float startingStability, Country country){
        stability = startingStability;
        this.country = country;
        stabilityModifier = new Modifier.create(Component.text("Stability", NamedTextColor.GREEN, TextDecoration.BOLD))
                .build();
    }
    public void addStability(float amount){
        stability+=amount;
    }
    public void newWeek(){
        float toAdd = country.getBoost(BoostEnum.stabilityBase)*country.getBoost(BoostEnum.stabilityGain);
        stability+=toAdd;
        stabilityModifier.setBoost(BoostEnum.production,(stability-50f)/100);
    }
    public float getStability(){
        return stability;
    }
}
