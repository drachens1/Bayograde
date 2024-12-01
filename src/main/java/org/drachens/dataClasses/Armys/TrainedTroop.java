package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainedTroop {
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
        HashMap<CurrencyTypes, Payment> payments2 = design2.getCost();
        HashMap<CurrencyTypes, Payment> payments1 = design1.getCost();
        HashMap<CurrencyTypes, Payment> difference = new HashMap<>();
        for (Map.Entry<CurrencyTypes, Payment> entry : payments1.entrySet()) {
            float amount = payments2.get(entry.getKey()).getAmount() - entry.getValue().getAmount();
            difference.put(entry.getKey(), new Payment(entry.getKey(), amount));
        }
        List<Float> fulfillment = new ArrayList<>();
        for (Map.Entry<CurrencyTypes, Payment> entry : difference.entrySet()) {
            float amount = country.subtractMaximumAmountPossible(entry.getValue());
            fulfillment.add(amount / payments2.get(entry.getKey()).getAmount());
        }
        strength = calculateMean(fulfillment);
        time = design2.calculateTime();
    }

    private void calculateStrength() {
        List<Float> fulfillment = new ArrayList<>();
        for (Map.Entry<CurrencyTypes, Payment> entry : design.getCost().entrySet()) {
            float amount = country.subtractMaximumAmountPossible(entry.getValue());
            fulfillment.add(amount / design.getCost().get(entry.getKey()).getAmount());
        }
        strength = calculateMean(fulfillment);
    }

    private float calculateMean(List<Float> stuff) {
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
}
