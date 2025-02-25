package org.drachens.generalGame.troops;

import com.google.gson.JsonElement;
import lombok.Getter;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.List;

public class Combat implements Saveable {
    @Getter
    private final Province province;
    private final List<Troop> attackers = new ArrayList<>();
    private final List<Troop> defenders = new ArrayList<>();
    private boolean isActive;

    public Combat(Province province) {
        this.province = province;
        this.isActive = true;
        province.setCombat(this);
    }

    public void addAttackers(List<Troop> troops){
        troops.forEach(this::addAttacker);
    }

    public void addAttacker(Troop troop) {
        attackers.add(troop);
        troop.setBattle(this);
        troop.startShootingAnimation();
    }

    public void removeAttacker(Troop troop) {
        attackers.remove(troop);
        troop.setBattle(null);
        troop.stopShootingAnimation();
    }

    public void addDefenders(List<Troop> troops){
        troops.forEach(this::addDefender);
    }

    public void addDefender(Troop troop) {
        defenders.add(troop);
        troop.setBattle(this);
        troop.startShootingAnimation();
    }

    public void removeDefender(Troop troop) {
        defenders.remove(troop);
        troop.setBattle(null);
        troop.stopShootingAnimation();
    }


    public void newDay() {
        resolveAttack();
    }

    private void resolveAttack() {
        for (Troop attacker : new ArrayList<>(attackers)) {
            if (attacker.isDead()){
                removeAttacker(attacker);
            }
            for (Troop defender : new ArrayList<>(defenders)) {
                if (defender.isDead()){
                    removeDefender(defender);
                }
                float attackerDamage = attacker.getDamage() * (attacker.getStrength() / 100);
                float defenderDamage = defender.getDamage() * (defender.getStrength() / 100);

                float attackerDamageReduction = defender.getDefence() / (defender.getDefence() + attacker.getDamage());
                float defenderDamageReduction = attacker.getDefence() / (attacker.getDefence()*1.1f + defender.getDamage());

                defender.setHealth(defender.getHealth() - (attackerDamage * (1 - defenderDamageReduction)));
                defender.setOrg(defender.getOrg() - (attackerDamage * (1 - defenderDamageReduction) * 0.2f));

                attacker.setHealth(attacker.getHealth() - (defenderDamage * (1 - attackerDamageReduction)));
                attacker.setOrg(attacker.getOrg() - (defenderDamage * (1 - attackerDamageReduction) * 0.2f));

                if (0 >= defender.getOrg()) {
                    defender.retreat();
                    removeDefender(defender);
                } else if (0 >= defender.getHealth()) {
                    defender.kill();
                }

                if (0 >= attacker.getOrg()) {
                    attacker.retreat();
                    removeAttacker(attacker);
                } else if (0 >= attacker.getHealth()) {
                    attacker.kill();
                }
            }
        }

        if (attackers.isEmpty() || defenders.isEmpty()) {
            isActive = false;
            ContinentalManagers.combatManager.removeCombat(this);
        }
    }


    public boolean isActive() {
        return isActive;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
