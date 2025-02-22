package org.drachens.dataClasses.Countries;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.drachens.Manager.defaults.enums.IdeologiesEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Leader implements Saveable {
    private final List<Modifier> modifiers;
    private final Component name;
    private Component description;
    private IdeologyTypes ideologyTypes;

    public Leader(create leaderBuilder) {
        name = leaderBuilder.name;
        if (null != leaderBuilder.description) description = leaderBuilder.description;
        modifiers = leaderBuilder.modifier;
    }

    public List<Modifier> getModifier() {
        return modifiers;
    }

    public void createDescription() {
        if (null == this.description) {
            List<Component> modifierComps = new ArrayList<>();
            for (Modifier modifier : modifiers) {
                if (modifier.shouldDisplay()) modifierComps.add(Component.text()
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

    public void setIdeologyTypes(IdeologiesEnum ideologyTypes) {
        this.ideologyTypes = ideologyTypes.getIdeologyTypes();
        createDescription();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive("bob");
    }

    public static class create {
        private final Component name;
        private final List<Modifier> modifier = new ArrayList<>();
        private Component description;

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

        public Leader build() {
            return new Leader(this);
        }
    }
}
