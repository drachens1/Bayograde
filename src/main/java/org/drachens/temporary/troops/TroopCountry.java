package org.drachens.temporary.troops;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.DivisionTypeEnum;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue;
import org.drachens.dataClasses.Armys.DivisionType;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.CountryEnums;
import org.drachens.dataClasses.Countries.Election;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.territories.Province;
import org.drachens.events.NewDay;
import org.drachens.temporary.clicks.ClicksVault;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TroopCountry extends ResearchCountry {
    private CountryEnums.Type type;
    private CountryEnums.RelationsStyle relationsStyle;
    private CountryEnums.History history;
    private CountryEnums.Focuses focuses;
    private CountryEnums.PreviousWar previousWar;
    private final List<Clientside> allyTroopClientsides = new ArrayList<>();
    private final List<Troop> troops = new ArrayList<>();
    private final List<DivisionDesign> divisionDesigns = new ArrayList<>();
    private final HashMap<Building, DivisionTrainingQueue> divisionTrainingQueueHashMap = new HashMap<>();

    public TroopCountry(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance) {
        super(name, nameComponent, block, border, defaultIdeologies, election, instance, new ClicksVault(startingCurrencies));

        HashMap<Integer, DivisionType> norm = new HashMap<>();
        int[] slots = new int[]{12,13,14,21,22,23,30,31,32};
        for (int i : slots){
            norm.put(i, DivisionTypeEnum.ww2_infantry.getDivisionType());
        }
        divisionDesigns.add(new DivisionDesign("Womp",norm,this));
        divisionDesigns.add(new DivisionDesign("Womp",norm,this));
        divisionDesigns.add(new DivisionDesign("Womp",norm,this));
        divisionDesigns.add(new DivisionDesign("Womp",norm,this));
        divisionDesigns.add(new DivisionDesign("Womp",norm,this));

    }

    @Override
    protected void onAddPlayer(CPlayer p) {

    }

    @Override
    protected void onRemovePlayer(CPlayer p) {

    }

    @Override
    public void newWeek(NewDay newDay) {

    }

    @Override
    public void newDay(NewDay newDay){
        new HashMap<>(divisionTrainingQueueHashMap).forEach(((building, divisionTrainingQueue) -> divisionTrainingQueue.newDay()));
    }

    @Override
    public void removeOccupied(Province province){
        super.removeOccupied(province);
        if (province.getBuilding()!=null){
            divisionTrainingQueueHashMap.remove(province.getBuilding());
        }
    }

    public DivisionTrainingQueue getBuildingsTraining(Building building){
        DivisionTrainingQueue divisionTrainingQueue = divisionTrainingQueueHashMap.getOrDefault(building,new DivisionTrainingQueue(building));
        divisionTrainingQueueHashMap.putIfAbsent(building,divisionTrainingQueue);
        return divisionTrainingQueue;
    }

    public void finishBuildingTraining(Building building){
        divisionTrainingQueueHashMap.remove(building);
    }

    public void addDivisionDesign(DivisionDesign divisionDesign){
        this.divisionDesigns.add(divisionDesign);
    }

    public void removeDivisionDesign(DivisionDesign divisionDesign){
        this.divisionDesigns.remove(divisionDesign);
    }

    public List<DivisionDesign> getDivisionDesigns(){
        return divisionDesigns;
    }

    public CountryEnums.Type getType() {
        return type;
    }

    public void setType(CountryEnums.Type newType) {
        type = newType;
    }

    public CountryEnums.History getHistory() {
        return history;
    }

    public void setHistory(CountryEnums.History history) {
        this.history = history;
    }

    public CountryEnums.Focuses getFocuses() {
        return focuses;
    }

    public void setFocuses(CountryEnums.Focuses f) {
        this.focuses = f;
    }

    public CountryEnums.PreviousWar getPreviousWar() {
        return previousWar;
    }

    public void setPreviousWar(CountryEnums.PreviousWar p) {
        this.previousWar = p;
    }

    public CountryEnums.RelationsStyle getRelationsStyle() {
        return relationsStyle;
    }

    public void setRelationsStyle(CountryEnums.RelationsStyle relationsStyle) {
        this.relationsStyle = relationsStyle;
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
        loadClientside(troop.getTroop());
        allyTroopClientsides.add(troop.getAlly());
    }

    public void removeTroop(Troop troop) {
        troops.remove(troop);
        unloadClientside(troop.getTroop());
        allyTroopClientsides.add(troop.getAlly());
    }

    public List<Clientside> getAlliedTroopClientsides() {
        return allyTroopClientsides;
    }
}
