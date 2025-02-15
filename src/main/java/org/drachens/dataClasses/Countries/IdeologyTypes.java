package org.drachens.dataClasses.Countries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.interfaces.Saveable;

import java.util.List;

public class IdeologyTypes implements Saveable {
    private final List<Leader> leaders;
    private final String identifier;
    private final Component prefix;
    private final Modifier modifier;
    private Component name;

    public IdeologyTypes(TextColor colour, String prefix, String name, List<Leader> leaders, Modifier modifier) {
        this.name = Component.text(name, colour);
        this.prefix = Component.text(prefix, colour);
        this.identifier = name;
        this.leaders = leaders;
        this.leaders.forEach((leader -> leader.setIdeologyTypes(this)));
        this.modifier = modifier;
        modifier.setShouldDisplay(false);
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

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("identifier",new JsonPrimitive(identifier));
        return jsonObject;
    }
}
