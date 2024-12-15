package org.drachens.dataClasses.Economics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;

import static org.drachens.util.OtherUtil.bound;

public class Stability {
    private final Country country;
    private float stabilityTotal;
    private float visibleStability;
    private final Modifier stabilityModifier;
    private float prevBase;

    public Stability(float startingStability, Country country) {
        stabilityTotal = startingStability;
        this.country = country;
        stabilityModifier = new Modifier.create(Component.text("Stability", NamedTextColor.GREEN, TextDecoration.BOLD))
                .build();
    }

    public void newWeek() {
        float stabilityBase = country.getBoost(BoostEnum.stabilityBase);
        if (prevBase != stabilityBase) {
            stabilityTotal += stabilityBase;
            stabilityBase -= prevBase;
        }
        prevBase = stabilityBase;
        float stabilityGain = country.getBoost(BoostEnum.stabilityGain);
        stabilityTotal = stabilityTotal + stabilityGain;
        visibleStability = bound(100f, 0f, stabilityTotal);
        stabilityModifier.setBoost(BoostEnum.production, (visibleStability - 50f) / 100);
    }

    public float getStability() {
        return visibleStability;
    }
}
