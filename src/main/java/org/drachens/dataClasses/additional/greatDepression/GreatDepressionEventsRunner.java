package org.drachens.dataClasses.additional.greatDepression;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.Manager.defaults.enums.ColoursEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.EventsRunner;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.util.MessageEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GreatDepressionEventsRunner implements EventsRunner {
    private int timeSinceLast = 70;
    private boolean protectionismComp = false;
    private boolean devalueCurrencyComp = false;
    private boolean abandonGoldStandard = false;
    private boolean relief = false;
    private boolean recovery = false;
    private boolean reform = false;
    private final Country country;
    private final Modifier greatDepression;
    private final HashMap<BoostEnum, Float> boostHashMap = new HashMap<>();
    int count = 0;

    public GreatDepressionEventsRunner(Country country, Modifier modifier){
        this.country=country;
        greatDepression=modifier;
    }

    public void addBoost(BoostEnum boostEnum, float f) {
        float current = boostHashMap.getOrDefault(boostEnum,0f);
        boostHashMap.put(boostEnum,current+f);
    }

    private boolean completed = false;

    @Override
    public boolean newDay() {
        count++;
        timeSinceLast++;
        if (count<7){
            return false;
        }
        if (completed)return false;
        count=0;
        double current = Math.floor(greatDepression.getBoost(BoostEnum.production));
        if (current>0){
            country.removeModifier(greatDepression);
            completed = true;
            country.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have began your recovery from the great depression",NamedTextColor.GREEN))
                    .build());
            boostHashMap.remove(BoostEnum.production);
            Modifier recovery = new Modifier.create(Component.text("Recovery", ColoursEnum.ORANGE.getTextColor()),"recovery")
                    .addBoost(BoostEnum.production, +0.1f)
                    .addBoost(BoostEnum.stabilityBase, +10f)
                    .addBoost(BoostEnum.buildingSlotBoost, +1)
                    .addBoost(BoostEnum.capitulation, +0.1f)
                    .build();
            country.addModifier(recovery);
            return true;
        }
        greatDepression.addBoosts(boostHashMap);
        return false;
    }

    public Component getDescription(){
        List<Component> comps = new ArrayList<>();
        boostHashMap.forEach((boostEnum, value) -> {
            Component symbol;
            if (value<0){
                symbol=boostEnum.getNegSymbol();
            }else {
                symbol=boostEnum.getPosSymbol();
            }
            if (boostEnum.isPercentage()) {
                if (value > 0) {
                    comps.add(Component.text()
                            .append(Component.text("+" + Math.round(value * 100), NamedTextColor.GREEN))
                            .append(Component.text("%", NamedTextColor.GREEN))
                            .append(symbol)
                            .appendNewline().build());
                } else {
                    comps.add(Component.text()
                            .append(Component.text(Math.round(value * 100), NamedTextColor.RED))
                            .append(Component.text("%", NamedTextColor.RED))
                            .append(symbol)
                            .appendNewline().build());
                }
            } else {
                if (value > 0) {
                    comps.add(Component.text()
                            .append(Component.text("+" + value, NamedTextColor.GREEN))
                            .append(symbol)
                            .appendNewline().build());
                } else {
                    comps.add(Component.text()
                            .append(Component.text(value, NamedTextColor.RED))
                            .append(symbol)
                            .appendNewline().build());
                }
            }
        });
        return Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text("Weekly"))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(comps)
                .build();
    }

    public int getTimeSinceLast(){
        return timeSinceLast;
    }

    public void setTimeSinceLast(int timeSinceLast){
        this.timeSinceLast=timeSinceLast;
    }

    public boolean isAbandonGoldStandard() {
        return abandonGoldStandard;
    }

    public boolean isDevalueCurrencyComp() {
        return devalueCurrencyComp;
    }

    public boolean isProtectionismComp() {
        return protectionismComp;
    }

    public boolean isRecovery() {
        return recovery;
    }

    public boolean isReform() {
        return reform;
    }

    public boolean isRelief() {
        return relief;
    }

    public void setAbandonGoldStandard(boolean abandonGoldStandard) {
        this.abandonGoldStandard = abandonGoldStandard;
    }

    public void setDevalueCurrencyComp(boolean devalueCurrencyComp) {
        this.devalueCurrencyComp = devalueCurrencyComp;
    }

    public void setProtectionismComp(boolean protectionismComp) {
        this.protectionismComp = protectionismComp;
    }

    public void setRecovery(boolean recovery) {
        this.recovery = recovery;
    }

    public void setReform(boolean reform) {
        this.reform = reform;
    }

    public void setRelief(boolean relief) {
        this.relief = relief;
    }
}
