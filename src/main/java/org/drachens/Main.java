package org.drachens;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.ScoreboardManager;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.IdeologyTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.ServerUtil.setupAll;

public class Main {
    public static void main(String[] args) {
        ItemStack[] items = {};
        HashMap<IdeologyTypes, Float> ideologyTypes = new HashMap<>();
        ideologyTypes.put(new IdeologyTypes(compBuild("Fascist", NamedTextColor.BLACK),compBuild("F",NamedTextColor.BLACK)),5f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Authoritarian", NamedTextColor.GRAY),compBuild("A",NamedTextColor.BLACK)),2f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Democratic", NamedTextColor.BLUE),compBuild("D",NamedTextColor.BLACK)),51f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Oligarchy", NamedTextColor.BLUE),compBuild("O",NamedTextColor.BLACK)),11f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Anarchism", NamedTextColor.GREEN),compBuild("A",NamedTextColor.BLACK)),1f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Conservatism", NamedTextColor.AQUA),compBuild("Cons",NamedTextColor.BLACK)),24f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Corporatism", NamedTextColor.DARK_GREEN),compBuild("Corp",NamedTextColor.BLACK)),1f);
        ideologyTypes.put(new IdeologyTypes(compBuild("Socialism", NamedTextColor.RED),compBuild("S",NamedTextColor.BLACK)),5f);
        Ideology ideology = new Ideology(ideologyTypes);
        List<IdeologyTypes> ideologyTypesList = new ArrayList<>();
        for (Map.Entry<IdeologyTypes,Float> e : ideologyTypes.entrySet()){
            ideologyTypesList.add(e.getKey());
        }
        setupAll(new ArrayList<>(), 60, new HashMap<>(), items,ideologyTypesList,ideology,new ScoreboardManager(new ArrayList<>()));
    }
}