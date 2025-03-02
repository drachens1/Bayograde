package org.drachens.generalGame.troops;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.DivisionTypeEnum;
import org.drachens.dataClasses.Armys.*;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.events.NewDay;
import org.drachens.generalGame.clicks.ClicksVault;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class TroopCountry extends Country {
    private final List<Frontline> frontlines = new ArrayList<>();
    private final HashMap<String, Frontline> frontlineHashMap = new HashMap<>();
    private final List<Clientside> allyTroopClientsides = new ArrayList<>();
    private final List<Troop> troops = new ArrayList<>();
    private final List<DivisionDesign> divisionDesigns = new ArrayList<>();
    private final HashMap<Building, DivisionTrainingQueue> divisionTrainingQueueHashMap = new HashMap<>();
    private final HashMap<CPlayer, String> activeFrontLine = new HashMap<>();

    public TroopCountry(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Instance instance, HashMap<String, LawCategory> laws) {
        super(name, nameComponent, block, border, defaultIdeologies, instance, new ClicksVault(startingCurrencies), laws);

        HashMap<Integer, DivisionType> norm = new HashMap<>();
        int[] slots = {12, 13, 14, 21, 22, 23, 30, 31, 32};
        for (int i : slots) {
            norm.put(i, DivisionTypeEnum.ww2_infantry.getDivisionType());
        }
        divisionDesigns.add(new DivisionDesign("Womp", norm, this));
        divisionDesigns.add(new DivisionDesign("Womp", norm, this));
        divisionDesigns.add(new DivisionDesign("Womp", norm, this));
        divisionDesigns.add(new DivisionDesign("Womp", norm, this));
        divisionDesigns.add(new DivisionDesign("Womp", norm, this));
    }

    @Override
    protected void onAddPlayer(CPlayer p) {}

    @Override
    protected void onRemovePlayer(CPlayer p) {}

    @Override
    public void newWeek(NewDay newDay) {

    }

    @Override
    public void newDay(NewDay newDay) {
        new HashMap<>(divisionTrainingQueueHashMap).forEach((building, divisionTrainingQueue) -> divisionTrainingQueue.newDay());
    }

    @Override
    protected JsonElement abstractToJson() {
        return new JsonObject();
    }

    @Override
    public void removeOccupied(Province province) {
        super.removeOccupied(province);
        if (null != province.getBuilding()) {
            divisionTrainingQueueHashMap.remove(province.getBuilding());
        }
    }

    public DivisionTrainingQueue getBuildingsTraining(Building building) {
        DivisionTrainingQueue divisionTrainingQueue = divisionTrainingQueueHashMap.getOrDefault(building, new DivisionTrainingQueue(building));
        divisionTrainingQueueHashMap.putIfAbsent(building, divisionTrainingQueue);
        return divisionTrainingQueue;
    }

    public void finishBuildingTraining(Building building) {
        divisionTrainingQueueHashMap.remove(building);
    }

    public void addDivisionDesign(DivisionDesign divisionDesign) {
        this.divisionDesigns.add(divisionDesign);
    }

    public void removeDivisionDesign(DivisionDesign divisionDesign) {
        this.divisionDesigns.remove(divisionDesign);
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
        getInfo().addClientside(troop.getTroop());
        allyTroopClientsides.add(troop.getAlly());
    }

    public void removeTroop(Troop troop) {
        troops.remove(troop);
        getInfo().removeClientside(troop.getTroop());
        allyTroopClientsides.add(troop.getAlly());
    }

    public List<Clientside> getAlliedTroopClientsides() {
        return allyTroopClientsides;
    }

    public void addFrontLine(String key, Frontline frontline){
        frontlineHashMap.put(key,frontline);
        frontlines.add(frontline);
    }

    public void removeFrontLine(String key, Frontline frontline){
        frontlineHashMap.remove(key);
        frontlines.remove(frontline);
    }

    public boolean hasFrontLine(String key){
        return frontlineHashMap.containsKey(key);
    }

    public Frontline getFrontLine(String key){
        return frontlineHashMap.get(key);
    }

    public boolean activeFrontLineContainsPlayer(CPlayer player){
        return activeFrontLine.containsKey(player);
    }

    public void addActiveFrontLine(CPlayer player, String key){
        activeFrontLine.put(player,key);
    }

    public String getActiveKeyFrontLine(CPlayer p){
        return activeFrontLine.get(p);
    }

    public Frontline getActiveFrontLine(CPlayer p){
        return frontlineHashMap.get(activeFrontLine.get(p));
    }

    public Frontline getFrontLine(int index){
        return frontlines.get(index);
    }
}
