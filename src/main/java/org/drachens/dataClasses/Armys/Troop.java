package org.drachens.dataClasses.Armys;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue.TrainedTroop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.temporary.troops.Combat;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.util.AStarPathfinderXZ;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Troop {
    private final TroopType troopType;
    private final TroopCountry country;
    private final AStarPathfinderVoids troopPathing;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private final ItemDisplay troop;
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
    private final HashSet<Country> countries = new HashSet<>();

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
        startMoveAnimation();
        if (province.distance(to)<=1){
            task = scheduler.buildTask(()->{
                moveBlock(to);
            }).delay(1200, ChronoUnit.MILLIS).schedule();
            return;
        }
        task = scheduler.buildTask(new Runnable() {
            final List<AStarPathfinderXZ.Node> path = country.getaStarPathfinder().findPath(province, to, country, troopPathing);
            int current = 0;

            @Override
            public void run() {
                if (path.size() <= current) {
                    task.cancel();
                    stopAnimations();
                    return;
                }
                moveBlock(path.get(current).province);
                current++;
            }
        }).repeat(1200, ChronoUnit.MILLIS).schedule();
    }

    public void moveBlock(Province to) {
        if (!canMove(to)) return;

        if (to.getOccupier() == country || to.getOccupier().isMilitaryFriend(country)) {
            moveToFriendly(to);
        } else
            moveToEnemy(to);

        countries.clear();
        Set<Country> removed = new HashSet<>(countries);
        to.getNeighbours().forEach(p -> {
            Country occp = p.getOccupier();
            if (country!=occp){
                if (!countries.contains(occp)){
                    countries.add(occp);
                    int diplo = country.getDiplomacy(occp.getName());
                    if (!(diplo==6||diplo==5)){
                        occp.addClientside(enemy);
                    }
                }
                removed.remove(occp);
            }
        });

        removed.forEach(country1 -> country1.removeClientside(enemy));
    }

    public boolean canMove(Province province) {
        return province != null;
    }

    public void moveToFriendly(Province to) {
        if (t!=null)t.cancel();
        t = scheduler.buildTask(this::cull).delay(600,ChronoUnit.MILLIS).schedule();
        smoothMove(to);
        moveDisplays(to);
        move(to, province);
        province = to;
    }

    public void moveToEnemy(Province to) {
        if (!to.getTroops().isEmpty()) {
            attack(to);
        }
        smoothMove(to);
        moveDisplays(to);
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
            Combat c = new Combat(province);
            c.addAttacker(this);
            c.addDefenders(province.getTroops());
            ContinentalManagers.combatManager.addCombat(c);
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
            if (province.getTroops().getFirst()!=this){
                hideDisplays();
            }
        }else {
            showDisplays();
        }
    }

    private void startMoveAnimation(){
        troopType.getMoveAnimation().start(troop, true).onFinish(troop, () -> troop.setItem(troopType.getOwnTroop()));
        troopType.getEnemyMoveAnimation().start(enemy, true).onFinish(enemy, () -> enemy.setItem(troopType.getEnemyTroop()));
        troopType.getAllyMoveAnimation().start(ally, true).onFinish(ally, () -> ally.setItem(troopType.getAllyTroop()));
    }

    public void startShootingAnimation(){
        troopType.getShootingAnimation().start(troop);
        troopType.getAllyShootingAnimation().start(ally);
        troopType.getEnemyShootingAnimation().start(enemy);
    }

    private void stopMoveAnimation(){
        troopType.getMoveAnimation().stop(troop);
        troopType.getEnemyMoveAnimation().stop(enemy);
        troopType.getAllyMoveAnimation().stop(ally);
    }

    public void stopShootingAnimation(){
        troopType.getShootingAnimation().stop(troop);
        troopType.getAllyShootingAnimation().stop(ally);
        troopType.getEnemyShootingAnimation().stop(enemy);
    }

    private void stopAnimations(){
        if (troop.getAnimation()!=null){
            troop.getAnimation().stop(troop);
        }
        if (ally.getAnimation()!=null){
            ally.getAnimation().stop(ally);
        }
        if (enemy.getAnimation()!=null){
            enemy.getAnimation().stop(enemy);
        }
    }

    private void smoothMove(Province to){
        troop.moveSmooth(to, 20);
        ally.moveSmooth(to, 20);
        enemy.moveSmooth(to, 20);
    }

    private void moveDisplays(Province to){
        troop.setGhostPos(to.getPos());
        enemy.setGhostPos(to.getPos());
        ally.setGhostPos(to.getPos());
    }

    private void hideDisplays(){
        troop.hide();
        enemy.hide();
        ally.hide();
    }

    private void showDisplays(){
        troop.show();
        enemy.show();
        ally.show();
    }
}
