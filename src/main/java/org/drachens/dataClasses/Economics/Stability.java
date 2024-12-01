package org.drachens.dataClasses.Economics;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;

import static org.drachens.util.KyoriUtil.compBuild;

public class Stability {
    private final Country country;
    private float stability;
    private final Modifier stabilityModifier;
    public Stability(float startingStability, Country country){
        stability = startingStability;
        this.country = country;
        stabilityModifier = new Modifier.create(compBuild("Stability", NamedTextColor.GREEN, TextDecoration.BOLD))
                .build();
    }
    public void addStability(float amount){
        stability+=amount;
    }
    public void newWeek(){
        float toAdd = country.getWeeklyStabilityGain()*country.getStabilityGainBoost();
        stability+=toAdd;
        stabilityModifier.setProductionBoost((stability-50f)/100);
    }
    public float getStability(){
        return stability;
    }
}
