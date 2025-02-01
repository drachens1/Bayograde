package org.drachens.dataClasses.Armys;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue.TrainedTroop;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.temporary.troops.Combat;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.util.AStarPathfinderXZ;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class Troop {
    private final ItemDisplay troop;
    private final TroopType troopType;
    private final TroopCountry country;
    private final AStarPathfinderVoids troopPathing;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private final ItemDisplay ally;
    private final ItemDisplay enemy;
    private Province province;
    private Task task;
    private Combat battle;
    private float strength;
    private float health;
    private float damage;
    private float defence;
    private float speed;
    private float organisation;
    private DivisionDesign design;
    private Task t;

    public Troop(Province province, TrainedTroop trainedTroop, AStarPathfinderVoids troopPathing) {
        this.troopPathing = troopPathing;
        this.troopType = trainedTroop.getTroopType();
        this.country = trainedTroop.getCountry();
        Pos pos = province.getPos().add(0.5, 1.5, 0.5);
        this.troop = new ItemDisplay(troopType.getOwnTroop(), pos, province.getInstance(), ItemDisplay.DisplayType.GROUND, true);
        this.ally = new ItemDisplay(troopType.getAllyTroop(), pos, province.getInstance(), ItemDisplay.DisplayType.GROUND, true);
        this.enemy = new ItemDisplay(troopType.getEnemyTroop(), pos, province.getInstance(), ItemDisplay.DisplayType.GROUND, true);
        this.province = province;
        this.design = trainedTroop.getDesign();
        this.strength = trainedTroop.getStrength();
        this.health = design.getHp();
        this.damage = design.getAtk();
        this.defence = design.getDef();
        this.speed = design.getSpeed();
        this.country.addTroop(this);
        this.organisation = trainedTroop.getDesign().getOrg();
        province.addTroop(this);
        cull();
    }

    public float getOrg() {
        return organisation;
    }

    public void setOrg(float org) {
        this.organisation = org;
    }

    public Province getProvince() {
        return province;
    }

    public ItemDisplay getAlly() {
        return ally;
    }

    public ItemDisplay getEnemey() {
        return enemy;
    }

    public ItemDisplay getTroop() {
        return troop;
    }

    public float getSpeed() {
        return speed;
    }

    public void move(Province to) {
        if (task != null && task.isAlive()) task.cancel();
        troopType.getMoveAnimation().start(troop, true).onFinish(troop, () -> troop.setItem(troopType.getOwnTroop()));
        task = scheduler.buildTask(new Runnable() {
            final List<AStarPathfinderXZ.Node> path = country.getaStarPathfinder().findPath(province, to, country, troopPathing);
            int current = 0;

            @Override
            public void run() {
                if (path.size() <= current) {
                    task.cancel();
                    troopType.getMoveAnimation().stop(troop);
                    return;
                }
                moveBlock(path.get(current).province);
                current++;
            }
        }).repeat(1200, ChronoUnit.MILLIS).schedule();
    }

    public void moveBlock(Province to) {
        if (!canMove(to)) return;

        if (to.getOccupier() == country || to.getOccupier().isMilitaryAlly(country)) {
            moveToFriendly(to);
        } else
            moveToEnemy(to);

    }

    public boolean canMove(Province province) {
        return province != null;
    }

    public void moveToFriendly(Province to) {
        move(to, province);
        if (t!=null)t.cancel();
        t = scheduler.buildTask(this::cull).delay(600,ChronoUnit.MILLIS).schedule();
        troop.moveSmooth(to, 20);
        province = to;
    }

    public void moveToEnemy(Province to) {
        if (!to.getTroops().isEmpty()) {
            attack(to);
        }
        troop.moveSmooth(to, 20);
        move(to, province);
        province = to;
        province.setOccupier(country);
    }

    public void move(Province to, Province from) {
        from.removeTroop(this);
        to.addTroop(this);
    }

    public void attack(Province province) {
        if (province.isThereCombat()) {
            joinBattle(province.getCombat());
        } else {
            new Combat(province).addAttacker(this);
        }
    }

    public void joinBattle(Combat battle) {
        battle.addAttacker(this);
    }

    public void leaveBattle() {
        if (battle != null) {
            battle.removeAttacker(this);
        }
    }

    public void retreat() {

    }

    public Combat getBattle() {
        return battle;
    }

    public void setBattle(Combat battle) {
        this.battle = battle;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float amount) {
        this.health = amount;
    }

    public float getDefence() {
        return defence;
    }

    public float getStrength() {
        return damage;
    }

    public void setStrength(float s) {
        strength = s;
    }

    public float getDamage() {
        return damage;
    }

    public void kill() {
        troop.delete();
        ally.delete();
        enemy.delete();
    }

    public TroopCountry getCountry() {
        return country;
    }

    private void cull(){
        if (province.getTroops().size()>=2){
            if (province.getTroops().getFirst()!=this) troop.hide();
        }else {
            troop.show();
        }
    }
}
