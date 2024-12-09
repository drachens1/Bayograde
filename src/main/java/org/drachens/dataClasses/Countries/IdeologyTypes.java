package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Modifier;

import java.util.List;

public class IdeologyTypes {
    private final List<Leader> leaders;
    private final String identifier;
    private Component name;
    private final Component prefix;
    private final Modifier modifier;

    public IdeologyTypes(TextColor colour, String prefix, String name, List<Leader> leaders, Modifier modifier) {
        this.name = Component.text(name, colour);
        this.prefix = Component.text(prefix, colour);
        this.identifier = name;
        this.leaders = leaders;
        this.leaders.forEach((leader -> leader.setIdeologyTypes(this)));
        this.modifier = modifier;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public Component getPrefix() {
        return prefix;
    }

    public List<Leader> getLeaders() {
        return leaders;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Modifier getModifier() {
        return modifier;
    }
}
