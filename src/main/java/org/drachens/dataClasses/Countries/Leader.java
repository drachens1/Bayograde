package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Modifier;

import java.util.ArrayList;
import java.util.List;

public class Leader {
    private Component name;
    private Component description;
    private final List<Modifier> modifiers;
    private IdeologyTypes ideologyTypes;

    private Leader(create leaderBuilder) {
        name = leaderBuilder.name;
        if (leaderBuilder.description != null) description = leaderBuilder.description;
        modifiers = leaderBuilder.modifier;
        if (leaderBuilder.ideologyTypes != null) ideologyTypes = leaderBuilder.ideologyTypes;
    }

    public void rename(Component name) {
        this.name = name;
    }

    public Component getName() {
        return name;
    }

    public List<Modifier> getModifier() {
        return modifiers;
    }

    public Component getDescription() {
        return description;
    }

    public void createDescription() {
        if (description == null) {
            List<Component> modifierComps = new ArrayList<>();
            for (Modifier modifier : modifiers) {
                modifierComps.add(Component.text()
                        .appendNewline()
                        .append(modifier.getDescription())
                        .build());
            }
            description = Component.text()
                    .append(modifierComps)
                    .build();
        }
    }

    public void setIdeologyTypes(IdeologyTypes ideologyTypes) {
        this.ideologyTypes = ideologyTypes;
        createDescription();
    }

    public IdeologyTypes getIdeologyTypes() {
        return ideologyTypes;
    }

    public static class create {
        private final Component name;
        private Component description;
        private List<Modifier> modifier = new ArrayList<>();
        private IdeologyTypes ideologyTypes;

        public create(Component name) {
            this.name = name;
        }

        public create addModifier(Modifier modifier) {
            this.modifier.add(modifier);
            return this;
        }

        public create setDescription(Component description) {
            this.description = description;
            return this;
        }

        public create setIdeologyTypes(IdeologyTypes ideologyTypes) {
            this.ideologyTypes = ideologyTypes;
            return this;
        }

        public Leader build() {
            return new Leader(this);
        }
    }
}
