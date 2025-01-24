package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.greatDepression.GreatDepression;

public enum ModifiersEnum {
    great_depression(new Modifier.create(Component.text("Great Depression", ColoursEnum.RED.getTextColor()), "great_depression")
            .addBoost(BoostEnum.production, -0.5f)
            .addBoost(BoostEnum.stabilityBase, -10f)
            .addBoost(BoostEnum.relations, -5f)
            .addBoost(BoostEnum.gunCost, +0.2f)
            .addBoost(BoostEnum.buildingSlotBoost, -1)
            .addBoost(BoostEnum.capitulation, -0.1f)
            .addModifierCommand(new GreatDepression())
            .setDisplay(true)
            .build()),

    ww2_fascist(new Modifier.create(Component.text("Fascist", TextColor.color(204, 0, 0)), "fascist")
            .addBoost(BoostEnum.production, 0.15f)
            .addBoost(BoostEnum.stabilityBase, 5f)
            .addBoost(BoostEnum.stabilityGain, -0.05f)
            .addBoost(BoostEnum.capitulation, -0.1f)
            .addBoost(BoostEnum.justification, 0.4f)
            .build()),

    ww2_nationalist(new Modifier.create(Component.text("Nationalist", TextColor.color(204, 0, 0)), "nationalist")
            .addBoost(BoostEnum.stabilityBase, 15f)
            .addBoost(BoostEnum.stabilityGain, 0.15f)
            .addBoost(BoostEnum.production, 0.1f)
            .addBoost(BoostEnum.gunAccuracy, 0.1f)
            .addBoost(BoostEnum.justification, 0.25f)
            .build()),

    ww2_centrist(new Modifier.create(Component.text("Centrist", TextColor.color(96, 96, 96)), "centrist")
            .addBoost(BoostEnum.stabilityBase, 30f)
            .addBoost(BoostEnum.capitulation, -5f)
            .addBoost(BoostEnum.production, 0.1f)
            .build()),

    ww2_anarchist(new Modifier.create(Component.text("Anarchist", TextColor.color(7, 154, 12)), "anarchist")
            .addBoost(BoostEnum.stabilityBase, -80f)
            .addBoost(BoostEnum.stabilityGain, -3f)
            .addBoost(BoostEnum.capitulation, -0.5f)
            .addBoost(BoostEnum.production, 2f)
            .addBoost(BoostEnum.justification, 0.6f)
            .build()),

    ww2_conservatist(new Modifier.create(Component.text("Conservatism", TextColor.color(204, 0, 0)), "conservatist")
            .addBoost(BoostEnum.stabilityBase, 5f)
            .build()),

    ww2_socialist(new Modifier.create(Component.text("Socialist", TextColor.color(255, 0, 0)), "socialist")
            .addBoost(BoostEnum.stabilityBase, 30f)
            .addBoost(BoostEnum.stabilityGain, 0.25f)
            .addBoost(BoostEnum.production, 0.2f)
            .addBoost(BoostEnum.justification, -0.1f)
            .build()),

    ww2_liberalist(new Modifier.create(Component.text("Liberalist", TextColor.color(51, 253, 255)), "liberalist")
            .addBoost(BoostEnum.production, 0.2f)
            .build()),

    ww2_capitalist(new Modifier.create(Component.text("Capitalist", TextColor.color(0, 153, 0)), "capitalist")
            .addBoost(BoostEnum.stabilityBase, -5f)
            .addBoost(BoostEnum.production, 0.4f)
            .build()),

    ww2_imperialist(new Modifier.create(Component.text("Imperialist", TextColor.color(255, 204, 0)), "imperialist")
            .addBoost(BoostEnum.production, 0.25f)
            .addBoost(BoostEnum.stabilityBase, 20f)
            .addBoost(BoostEnum.stabilityGain, 0.1f)
            .addBoost(BoostEnum.capitulation, -10f)
            .addBoost(BoostEnum.justification, 0.3f)
            .build()),

    ww2_neutral(new Modifier.create(Component.text("Neutrality", NamedTextColor.GRAY), "neutrality")
            .addBoost(BoostEnum.capitulation, 0.2f)
            .addBoost(BoostEnum.stabilityBase, 15f)
            .addBoost(BoostEnum.production, 0.1f)
            .addCondition(ConditionEnum.cant_join_faction)
            .addCondition(ConditionEnum.cant_start_a_war)
            .build());


    private final Modifier modifier;

    ModifiersEnum(Modifier modifier) {
        this.modifier = modifier;
    }

    public Modifier getModifier() {
        return modifier;
    }
}
