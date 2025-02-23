package org.drachens.dataClasses.Countries.countryClass;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Countries.CountryChat;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Diplomacy.PuppetChat;
import org.drachens.dataClasses.Economics.Stability;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Info implements Saveable {
    private String name;
    private Material block;
    private Material border;
    private Province capital;
    private float capitulationPoints;
    private float maxCapitulationPoints;
    private boolean capitulated;
    private Component prefix;
    private Component description;
    private Component originalName;
    private Leader leader;
    private CPlayer playerLeader;
    private Country overlord;
    private final Instance instance;
    private final List<CPlayer> players;
    private final List<Clientside> clientsides = new ArrayList<>();
    private final CountryChat countryChat;
    private PuppetChat puppetChat;
    private final Ideology ideology;
    private final Stability stability;

    public Info(String name, Material block, Material border, Province capital,
                float capitulationPoints, float maxCapitulationPoints, boolean capitulated,
                Component prefix, Component description, Component originalName,
                Leader leader, CPlayer playerLeader, List<CPlayer> players, Instance instance,
                CountryChat countryChat, Ideology ideology, Stability stability) {
        this.name = name;
        this.block = block;
        this.border = border;
        this.capital = capital;
        this.capitulationPoints = capitulationPoints;
        this.maxCapitulationPoints = maxCapitulationPoints;
        this.capitulated = capitulated;
        this.prefix = prefix;
        this.description = description;
        this.originalName = originalName;
        this.leader = leader;
        this.playerLeader = playerLeader;
        this.players = players;
        this.instance = instance;
        this.countryChat = countryChat;
        this.ideology = ideology;
        this.stability = stability;
    }

    public void addMaxCapitulationPoints(float amount){
        maxCapitulationPoints +=amount;
    }

    public void minusMaxCapitulationPoints(float amount){
        maxCapitulationPoints -=amount;
    }

    public void addCapitulationPoints(float amount){
        capitulationPoints +=amount;
    }

    public void minusCapitulationPoints(float amount){
        capitulationPoints -=amount;
    }

    public void addPlayer(CPlayer player){
        players.add(player);
    }

    public void removePlayer(CPlayer player){
        players.add(player);
    }

    public boolean containsPlayer(CPlayer player){
        return players.contains(player);
    }

    public void addClientside(Clientside clientside){
        clientsides.add(clientside);
        this.players.forEach(clientside::addViewer);
    }

    public void addClientsides(List<Clientside> clientsides){
        clientsides.forEach(this::addClientside);
    }

    public void removeClientside(Clientside clientside){
        clientsides.remove(clientside);
        this.players.forEach(clientside::removeViewer);
    }

    public void removeClientsides(List<Clientside> clientsides){
        clientsides.forEach(this::removeClientside);
    }

    public boolean hasClientside(Clientside clientside){
        return clientsides.contains(clientside);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();

        json.add("name", new JsonPrimitive(name));
        json.add("block", new JsonPrimitive(block.name()));
        json.add("border", new JsonPrimitive(border.name()));
        json.add("capital", capital.toJson());
        json.add("capitulationPoints", new JsonPrimitive(capitulationPoints));
        json.add("maxCapitulationPoints", new JsonPrimitive(maxCapitulationPoints));
        json.add("capitulated", new JsonPrimitive(capitulated));
        json.add("prefix", new JsonPrimitive(PlainTextComponentSerializer.plainText().serialize(prefix)));
        json.add("description", gson.toJsonTree(PlainTextComponentSerializer.plainText().serialize(description)));
        json.add("originalName", gson.toJsonTree(PlainTextComponentSerializer.plainText().serialize(originalName)));
        json.add("leader", leader.toJson());
        json.add("ideology", ideology.toJson());
        json.add("stability", stability.toJson());

        return json;
    }
}
