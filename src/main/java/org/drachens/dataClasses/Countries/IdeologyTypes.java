package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Modifier;

import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class IdeologyTypes {
    private String identifier;
    private Component name;
    private Component prefix;
    private final List<Leader> leaders;
    private Modifier modifier;
    public IdeologyTypes(TextColor colour, String prefix, String name, List<Leader> leaders, Modifier modifier){
        this.name = compBuild(name,colour);
        this.prefix = compBuild(prefix,colour);
        this.identifier = name;
        this.leaders = leaders;
        this.leaders.forEach((leader -> leader.setIdeologyTypes(this)));
        this.modifier = modifier;
    }

    public Component getName() {
        return name;
    }

    public Component getPrefix() {
        return prefix;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }
    public List<Leader> getLeaders(){
        return leaders;
    }
    public String getIdentifier(){
        return identifier;
    }
    public Modifier getModifier(){
        return modifier;
    }
}
