package org.drachens.Manager.defaults.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Modifier;

public enum ModifiersEnum {
    example(new Modifier.create(Component.text("Example", NamedTextColor.GOLD))
            .setDescription(Component.text("description", NamedTextColor.BLUE))
            .addBoost(BoostEnum.production, 0.1f)
            .addBoost(BoostEnum.capitulation, 2f)
            .addBoost(BoostEnum.stabilityBase, 3f)
            .addBoost(BoostEnum.stabilityGain, 4f)
            .addBoost(BoostEnum.relations, 6f)
            .addBoost(BoostEnum.buildingSlotBoost, 2f)
            .build()),

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

    ww2_fascist(new Modifier.create(Component.text("War", TextColor.color(204, 0, 0), TextDecoration.BOLD))
            .addBoost(BoostEnum.production, 0.1f)
            .addBoost(BoostEnum.stabilityBase, 10f)
            .addBoost(BoostEnum.stabilityGain, -0.1f)
            .addBoost(BoostEnum.capitulation, 5f)
            .build()),
    ww2_centrist(new Modifier.create(Component.text("Centrist", TextColor.color(96, 96, 96), TextDecoration.BOLD))
            .addBoost(BoostEnum.stabilityBase, 50f)
            .addBoost(BoostEnum.capitulation, -5f)
            .addBoost(BoostEnum.production, 0.2f)
            .build()),
    ww2_anarchist(new Modifier.create(Component.text("Anarchist", TextColor.color(7, 154, 12)))
            .addBoost(BoostEnum.stabilityBase, -100f)
            .addBoost(BoostEnum.stabilityGain, -5f)
            .addBoost(BoostEnum.capitulation, 0.5f)
            .addBoost(BoostEnum.production, 5f)
            .build()),
    ww2_conservatist(new Modifier.create(Component.text("Conservatism", TextColor.color(204, 0, 0)))
            .addBoost(BoostEnum.stabilityBase, 1f)
            .build()),
    ww2_socialist(new Modifier.create(Component.text("Socialist", TextColor.color(255, 0, 0)))
            .addBoost(BoostEnum.stabilityBase, 40f)
            .addBoost(BoostEnum.stabilityGain, 0.2f)
            .addBoost(BoostEnum.production, 0.2f)
            .build()),
    ww2_liberalist(new Modifier.create(Component.text("Liberalist", TextColor.color(51, 253, 255)))
            .build()),
    ww2_capitalist(new Modifier.create(Component.text("Capitalist", TextColor.color(0, 153, 0)))
            .addBoost(BoostEnum.stabilityBase, -10f)
            .addBoost(BoostEnum.production, 0.5f)
            .build());

    private final Modifier modifier;
    ModifiersEnum(Modifier modifier){
        this.modifier=modifier;
    }
    public Modifier getModifier(){
        return modifier;
    }
}
