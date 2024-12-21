package org.drachens.dataClasses.Armys;

import java.util.HashSet;
import java.util.Map;

import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.territories.Province;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.temporary.troops.TroopPathing;

public class DivisionTrainingQueue {
    private final HashSet<TrainedTroop> divisionDesign = new HashSet<>();
    private final Building building;
    
    public DivisionTrainingQueue(Building building){
        this.building=building;
    }

    public void addToQueue(DivisionDesign design){
        float total = 0f;
        for (Map.Entry<Integer, DivisionType> e : design.getDesign().entrySet()){
            total+=e.getValue().getTrainingTime();
        }
        divisionDesign.add(new TrainedTroop(null, design, total));
    }

    public void removeFromQueue(DivisionDesign design){
        divisionDesign.remove(design);
    }

    public void newDay(){
        if (divisionDesign.isEmpty())return;
        TrainedTroop design = divisionDesign.iterator().next();
        design.subtractTime(1f);
        if (design.getTrainingTime()<=0f){
            finishTrainedTroop(design);
        }
    }

    private void finishTrainedTroop(TrainedTroop trainedTroop){
        Province province = building.getProvince();
        Troop troop = new Troop(province, trainedTroop, new TroopPathing());
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
