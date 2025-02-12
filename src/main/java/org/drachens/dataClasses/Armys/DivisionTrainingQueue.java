package org.drachens.dataClasses.Armys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.PathingEnum;
import org.drachens.Manager.defaults.enums.TroopTypeEnum;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
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
    private final Animation trainingAnimation = new Animation(500, Material.ORANGE_DYE, new int[]{20, 21, 22, 23});
    private int count = 0;
    private TrainedTroop trainedTroop;
    private float time;

    public DivisionTrainingQueue(Building building) {
        this.building = building;
        completionBarTextDisplay = new CompletionBarTextDisplay(building.getProvince().getPos().add(0, 2, 0), building.getCountry().getInstance(), TextColor.color(0, 100, 0), Component.text(""));
    }

    public void addToQueue(DivisionDesign design) {
        count++;
        updateX();
        float total = 0f;
        for (Map.Entry<Integer, DivisionType> e : design.getDesign().entrySet()) {
            total += e.getValue().getTrainingTime();
        }
        TrainedTroop troop = new TrainedTroop(TroopTypeEnum.ww2.getTroopTye(), design, total);
        if (divisionDesign.isEmpty()) {
            trainedTroop = troop;
            time = troop.time;
            building.getCountry().addClientside(completionBarTextDisplay.getTextDisplay());
            trainingAnimation.start(building.getItemDisplay(), true);
        }
        divisionDesign.add(troop);
    }

    public void removeFromQueue(TrainedTroop design) {
        divisionDesign.remove(design);
        count--;
        updateX();
    }

    public void updateX(){
        completionBarTextDisplay.setAdditional(Component.text()
                        .append(Component.text("x"))
                        .append(Component.text(count))
                .build());
    }

    public void newDay() {
        if (divisionDesign.isEmpty()) return;
        trainedTroop.subtractTime(1f);
        if (trainedTroop.getTrainingTime() <= 0f) {
            finishTrainedTroop(trainedTroop);
        } else {
            completionBarTextDisplay.setProgress(trainedTroop.getTrainingTime() / time);
        }
    }

    private void finishTrainedTroop(TrainedTroop trainedTroop) {
        Province province = building.getProvince();
        Troop troop = new Troop(province, trainedTroop, PathingEnum.ww2.getaStarPathfinderVoids());
        removeFromQueue(trainedTroop);
        TroopCountry troopCountry = (TroopCountry) building.getCountry();
        troopCountry.addTroop(troop);
        if (divisionDesign.isEmpty()) {
            completionBarTextDisplay.getTextDisplay().hide();
            troopCountry.finishBuildingTraining(building);
            trainingAnimation.stop(building.getItemDisplay());
            building.getItemDisplay().setItem(itemBuilder(Material.ORANGE_DYE, 19));
        } else {
            completionBarTextDisplay.setProgress(1f);
            TrainedTroop next = divisionDesign.getFirst();
            this.trainedTroop = next;
            time = next.time;
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

        public float getTrainingTime() {
            return time;
        }

        public void subtractTime(float time) {
            this.time -= time;
        }
    }
}
