package org.drachens.dataClasses.Economics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.interfaces.Saveable;

import static org.drachens.util.OtherUtil.bound;

public class Stability implements Saveable {
    private final Country country;
    private final Modifier stabilityModifier;
    private float stabilityTotal;
    private float visibleStability;
    private float prevBase;

    public Stability(float startingStability, Country country) {
        stabilityTotal = startingStability;
        this.country = country;
        stabilityModifier = new Modifier.create(Component.text("Stability", NamedTextColor.GREEN, TextDecoration.BOLD), "stability")
                .addBoost(BoostEnum.production, (startingStability - 50f) / 100)
                .build();

        country.addModifier(stabilityModifier);
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

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("total",new JsonPrimitive(stabilityTotal));
        jsonObject.add("visible",new JsonPrimitive(visibleStability));
        jsonObject.add("prevBase",new JsonPrimitive(prevBase));
        return jsonObject;
    }
}
