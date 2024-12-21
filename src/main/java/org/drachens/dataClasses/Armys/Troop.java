package org.drachens.dataClasses.Armys;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue.TrainedTroop;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.dataClasses.territories.Province;
import org.drachens.temporary.troops.TroopCountry;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class Troop {
    private final ItemDisplay troop;
    private final TroopType troopType;
    private final TroopCountry country;
    private final AStarPathfinderVoids troopPathing;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private ItemDisplay ally;
    private ItemDisplay enemy;
    private Province province;
    private Task task;
    private Battle battle;
    private float strength;
    private float health;
    private float damage;
    private float defence;
    private float speed;
    private DivisionDesign design;

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
        province.addTroop(this);
        //MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(troop::addViewer);
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

    public void move(Province to) {
        if (task != null && task.isAlive()) task.cancel();
        troopType.getMoveAnimation().start(troop, true).onFinish(troop, () -> troop.setItem(troopType.getOwnTroop()));
        task = scheduler.buildTask(new Runnable() {
            final List<Province> path = country.getaStarPathfinder().findPath(province, to, country, troopPathing);
            int current = 0;

            @Override
            public void run() {
                if (path.size() <= current) {
                    task.cancel();
                    troopType.getMoveAnimation().stop(troop);
                    return;
                }
                moveBlock(path.get(current));
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
        return province != null && province.isCapturable();
    }

    public void moveToFriendly(Province to) {
        move(to, province);
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

    public void attack(Province to) {
        new Battle(to, this);
    }

    public void joinBattle(Battle battle) {
        troopType.getShootingAnimation().stop(troop);
        setBattle(battle);
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public float getHealth() {
        return health;
    }

    public float getDefence() {
        return defence;
    }

    public float getStrength() {
        return damage;
    }

    public float getDamage() {
        return damage;
    }

    public void kill() {
        troop.delete();
        ally.delete();
        enemy.delete();
    }
}
