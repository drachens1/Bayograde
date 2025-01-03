package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;

public enum ModifiersEnum {
    ww2_super(new Modifier.create(Component.text("Super Power", NamedTextColor.GOLD, TextDecoration.BOLD))
            .addBoost(BoostEnum.stabilityBase, 10f)
            .addBoost(BoostEnum.production, 0.3f)
            .setDescription(Component.text()
                    .append(Component.text("The nation is the only super power meaning they are by far the strongest at the start.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                    .build())
            .build()),
    ww2_major(new Modifier.create(Component.text("Major", NamedTextColor.GOLD, TextDecoration.BOLD))
            .addBoost(BoostEnum.stabilityBase, 5f)
            .addBoost(BoostEnum.production, 0.1f)
            .setDescription(Component.text()
                    .append(Component.text("This nation is a major power its below super power but above minor.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                    .build())
            .build()),
    ww2_minor(new Modifier.create(Component.text("Minor", NamedTextColor.GOLD, TextDecoration.BOLD))
            .setDescription(Component.text()
                    .append(Component.text("Just a wee lil nation.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                    .build())
            .build()),

    ww2_fascist(new Modifier.create(Component.text("Fascist", TextColor.color(204, 0, 0), TextDecoration.BOLD))
            .addBoost(BoostEnum.production, 0.15f)
            .addBoost(BoostEnum.stabilityBase, 5f)
            .addBoost(BoostEnum.stabilityGain, -0.05f)
            .addBoost(BoostEnum.capitulation, -0.1f)
            .addBoost(BoostEnum.justification, 0.4f)
            .build()),

    ww2_nationalist(new Modifier.create(Component.text("Nationalist", TextColor.color(204, 0, 0), TextDecoration.BOLD))
            .addBoost(BoostEnum.stabilityBase, 15f)
            .addBoost(BoostEnum.stabilityGain, 0.15f)
            .addBoost(BoostEnum.production, 0.1f)
            .addBoost(BoostEnum.gunAccuracy, 0.1f)
            .addBoost(BoostEnum.justification, 0.25f)
            .build()),

    ww2_centrist(new Modifier.create(Component.text("Centrist", TextColor.color(96, 96, 96), TextDecoration.BOLD))
            .addBoost(BoostEnum.stabilityBase, 30f)
            .addBoost(BoostEnum.capitulation, -5f)
            .addBoost(BoostEnum.production, 0.1f)
            .build()),

    ww2_anarchist(new Modifier.create(Component.text("Anarchist", TextColor.color(7, 154, 12)))
            .addBoost(BoostEnum.stabilityBase, -80f)
            .addBoost(BoostEnum.stabilityGain, -3f)
            .addBoost(BoostEnum.capitulation, -0.5f)
            .addBoost(BoostEnum.production, 2f)
            .addBoost(BoostEnum.justification, 0.6f)
            .build()),

    ww2_conservatist(new Modifier.create(Component.text("Conservatism", TextColor.color(204, 0, 0)))
            .addBoost(BoostEnum.stabilityBase, 5f)
            .build()),

    ww2_socialist(new Modifier.create(Component.text("Socialist", TextColor.color(255, 0, 0)))
            .addBoost(BoostEnum.stabilityBase, 30f)
            .addBoost(BoostEnum.stabilityGain, 0.25f)
            .addBoost(BoostEnum.production, 0.2f)
            .addBoost(BoostEnum.justification, -0.1f)
            .build()),

    ww2_liberalist(new Modifier.create(Component.text("Liberalist", TextColor.color(51, 253, 255)))
            .addBoost(BoostEnum.production, 0.2f)
            .build()),

    ww2_capitalist(new Modifier.create(Component.text("Capitalist", TextColor.color(0, 153, 0)))
            .addBoost(BoostEnum.stabilityBase, -5f)
            .addBoost(BoostEnum.production, 0.4f)
            .build()),

    ww2_imperialist(new Modifier.create(Component.text("Imperialist", TextColor.color(255, 204, 0), TextDecoration.BOLD))
            .addBoost(BoostEnum.production, 0.25f)
            .addBoost(BoostEnum.stabilityBase, 20f)
            .addBoost(BoostEnum.stabilityGain, 0.1f)
            .addBoost(BoostEnum.capitulation, -10f)
            .addBoost(BoostEnum.justification, 0.3f)
            .build()),

    ww2_neutral(new Modifier.create(Component.text("Neutrality", NamedTextColor.GRAY))
            .addBoost(BoostEnum.capitulation, 0.2f)
            .addBoost(BoostEnum.stabilityBase, 15f)
            .addBoost(BoostEnum.production, 0.1f)
            .addCondition(ConditionEnum.cant_join_faction)
            .addCondition(ConditionEnum.cant_start_a_war)
            .build());


    private final Modifier modifier;
    ModifiersEnum(Modifier modifier){
        this.modifier=modifier;
    }
    public Modifier getModifier(){
        return modifier;
    }
}
