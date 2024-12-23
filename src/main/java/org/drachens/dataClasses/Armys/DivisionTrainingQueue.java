package org.drachens.dataClasses.Armys;

import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.TroopTypeEnum;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
import org.drachens.dataClasses.territories.Province;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.temporary.troops.TroopPathing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DivisionTrainingQueue {
    private final CompletionBarTextDisplay completionBarTextDisplay;
    private final List<TrainedTroop> divisionDesign = new ArrayList<>();
    private final Building building;
    private TrainedTroop trainedTroop;
    private float time;
    private final Animation trainingAnimation = new Animation(500, Material.ORANGE_DYE,new int[]{23,24,25,26});

    public DivisionTrainingQueue(Building building){
        this.building=building;
        completionBarTextDisplay=new CompletionBarTextDisplay(building.getProvince().getPos().add(0,2,0), building.getCountry().getInstance(), TextColor.color(0,100,0));
    }

    public void addToQueue(DivisionDesign design){
        float total = 0f;
        for (Map.Entry<Integer, DivisionType> e : design.getDesign().entrySet()){
            total+=e.getValue().getTrainingTime();
        }
        TrainedTroop troop = new TrainedTroop(TroopTypeEnum.ww2.getTroopTye(), design, total);
        if (divisionDesign.isEmpty()){
            trainedTroop=troop;
            time=troop.time;
            building.getCountry().addTextDisplay(completionBarTextDisplay.getTextDisplay());
            trainingAnimation.start(building.getItemDisplay(),true);
        }
        divisionDesign.add(troop);
    }

    public void removeFromQueue(TrainedTroop design){
        divisionDesign.remove(design);
    }

    public void newDay(){
        if (divisionDesign.isEmpty())return;
        trainedTroop.subtractTime(1f);
        if (trainedTroop.getTrainingTime()<=0f){
            finishTrainedTroop(trainedTroop);
        }else {
            completionBarTextDisplay.setProgress(trainedTroop.getTrainingTime()/time);
        }
    }

    private void finishTrainedTroop(TrainedTroop trainedTroop){
        System.out.println("3");
        Province province = building.getProvince();
        Troop troop = new Troop(province, trainedTroop, new TroopPathing());
        removeFromQueue(trainedTroop);
        TroopCountry troopCountry = (TroopCountry) building.getCountry();
        troopCountry.addTroop(troop);
        if (divisionDesign.isEmpty()){
            completionBarTextDisplay.getTextDisplay().dispose();
            troopCountry.finishBuildingTraining(building);
            trainingAnimation.stop(building.getItemDisplay());
            building.getItemDisplay().setItem(itemBuilder(Material.ORANGE_DYE,22));
        }else {
            completionBarTextDisplay.setProgress(1f);
            TrainedTroop next = divisionDesign.getFirst();
            this.trainedTroop=next;
            time=next.time;
        }
    }

    public static class TrainedTroop {
        private final TroopCountry country;
        private final TroopType troopType;
        private float strength = 100f;
        private DivisionDesign design;
        private float time;
    
        public TrainedTroop(TroopType troopType, DivisionDesign design, float time) {
            this.troopType = troopType;
            this.design = new DivisionDesign(design);
            this.country = design.getCountry();
            this.time = time;
            calculateStrength();
        }
    
        public void updateDesign(DivisionDesign design) {
            calculateCostDifference(design, this.design);
            this.design = design;
        }
    
        public void calculateCostDifference(DivisionDesign design1, DivisionDesign design2) {
            Payments payments2 = design2.getCost();
            Payments payments1 = design1.getCost();
            Payments difference = new Payments();
            difference.addPayments(payments1);
            difference.minusPayments(payments2);
            difference.compress();
            HashSet<Float> floats = new HashSet<>();
            difference.getPayments().forEach(payment -> floats.add(payment.getAmount()));
            strength = calculateMean(floats);
            time = design2.calculateTime();
        }
    
        private void calculateStrength() {
            HashSet<Float> fulfillment = new HashSet<>();
            design.getCost().getPayments().forEach(payment -> {
                float amount = country.subtractMaximumAmountPossible(payment);
                fulfillment.add(amount / payment.getAmount());
            });
            strength = calculateMean(fulfillment);
        }
    
        private float calculateMean(HashSet<Float> stuff) {
            float total = 0f;
            int num = 0;
            for (Float f : stuff) {
                total += f;
                num++;
            }
            return total / num;
        }
    
        public TroopType getTroopType() {
            return troopType;
        }
    
        public float getStrength() {
            return strength;
        }
    
        public TroopCountry getCountry() {
            return country;
        }
    
        public DivisionDesign getDesign() {
            return design;
        }
    
        public float getTrainingTime(){
            return time;
        }
    
        public void subtractTime(float time){
            this.time-=time;
        }
    }
}
