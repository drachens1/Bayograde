package org.drachens.temporary.troops;

import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.List;

public class Combat {
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
        System.out.println("New combat day!");
        resolveAttack();
    }

    private void resolveAttack() {
        for (Troop attacker : new ArrayList<>(attackers)) {
            for (Troop defender : new ArrayList<>(defenders)) {
                float attackerDamage = attacker.getDamage() * attacker.getStrength();
                float defenderDamage = defender.getDamage() * defender.getStrength();

                float attackerDamageReduction = defender.getDefence() / (defender.getDefence() + attacker.getDamage());
                float defenderDamageReduction = attacker.getDefence() / (attacker.getDefence() + defender.getDamage());

                defender.setHealth(defender.getHealth() - (attackerDamage * (1 - defenderDamageReduction)));
                defender.setOrg(defender.getOrg() - (attackerDamage * (1 - defenderDamageReduction) * 0.2f));

                attacker.setHealth(attacker.getHealth() - (defenderDamage * (1 - attackerDamageReduction)));
                attacker.setOrg(attacker.getOrg() - (defenderDamage * (1 - attackerDamageReduction) * 0.2f));

                if (defender.getHealth() <= 0 || defender.getOrg() <= 0) {
                    defender.retreat();
                    defenders.remove(defender);
                }

                if (attacker.getHealth() <= 0 || attacker.getOrg() <= 0) {
                    attacker.retreat();
                    attackers.remove(attacker);
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

    public Province getProvince() {
        return province;
    }
}
