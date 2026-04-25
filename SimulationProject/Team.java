import greenfoot.Greenfoot;
import greenfoot.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Team extends Actor {
    public static final int RED = 0;
    public static final int BLUE = 1;

    private static int buildingMaximumAtempt = 200;

    private final int teamId;
    private final String name;
    private int resources;
    private final ArrayList<People> units;
    private final ArrayList<Buildings> buildings;
    private ArrayList<Barrack> barracks;
    private ArrayList<Turret> defensiveTurrets;
    private ArrayList <Worker> workers;
    private ArrayList<SupplyBot> supplybots;

    private String strategy;
    private static String[] strateges = {"ECO", "ATK", "DEF", "REG"};

    private int workerCoolDown = 0;
    private int marineCoolDown = 0;
    private int supplyBotCoolDown = 0;

    private int workerNeeded;
    private int currentWorkerAmount;

    private int marineNeeded;
    private int currentMarinedAmount;

    private int supplyBotNeeded;
    private int currentSupplyBotAmount;

    private World world;
    private int currentBarrackAmount;
    private int barrackNeeded;
    private int totalBarracksAmt;

    private int currentTurretAmount;
    private int turretNeeded;

    private int barrackCost = 125;
    private int turretCost = 150;

    private World w;

    private Base base;

    private String previousStrategy = "noStrategy";

    public Team(int teamId, String name, int startingMoney, String strategy, World w) {
        this.teamId = teamId;
        this.name = name;
        this.resources = startingMoney;
        this.units = new ArrayList<People>();
        this.buildings = new ArrayList<Buildings>();
        this.workers = new ArrayList<Worker>();
        this.supplybots = new ArrayList<SupplyBot>();

        this.barracks = new ArrayList<Barrack>();
        this.defensiveTurrets = new ArrayList<Turret>();

        this.strategy = strategy;
        this.world = w;
        this.w = w;

        workerNeeded = 0;
        currentWorkerAmount = 0;

        marineNeeded = 0;
        currentMarinedAmount = 0;

        currentBarrackAmount = 0;
        currentTurretAmount = 0;

        barrackNeeded = 0;
        currentBarrackAmount = 0;

        supplyBotNeeded = 0;
        currentSupplyBotAmount = 0;

    }

    public void act() {

        cleanWorkers();
        currentWorkerAmount = workers.size();

        if (workerCoolDown > 0) workerCoolDown--;
        if (marineCoolDown > 0) marineCoolDown--;

        if (currentWorkerAmount < workerNeeded) {
            if (workerCoolDown == 0 && resources >= Worker.getCost() && base != null && base.addPeople()) {
                currentWorkerAmount++;
                workerCoolDown = Worker.getMaxWorkerCoolDown();
            }
        }

        if (currentBarrackAmount < barrackNeeded) {
            if (resources >= barrackCost) {
                Barrack barrack = new Barrack(this);
                if (placeBarrack(barrack)) {
                    barracks.add(barrack);
                    currentBarrackAmount++;
                    resources -= barrackCost;
                }
            }
        }

        if (isBarrackExist()) {
            if (currentMarinedAmount < marineNeeded) {
                if (marineCoolDown == 0 && resources >= Marine.getCost()) {
                    Barrack barrack = barracks.get(Greenfoot.getRandomNumber(barracks.size()));
                    if (barrack.addPeople()) {
                        currentMarinedAmount++;
                        marineCoolDown = Marine.getMaxMarineCoolDown();
                    }
                }
            }
        }

        if (currentSupplyBotAmount < supplyBotNeeded) {
            if (supplyBotCoolDown == 0 && resources >= SupplyBot.getCost()) {
                if (base.addBot()) {
                    currentSupplyBotAmount++;
                    supplyBotCoolDown = SupplyBot.getMaxSupplyBotCoolDown();
                }
            }
        }

        if (currentTurretAmount < turretNeeded) {
            if (resources >= turretCost) {
                Turret turret = new Turret(this);
                if (placeTurret(turret)) {
                    defensiveTurrets.add(turret);
                    currentTurretAmount++;
                    resources -= turretCost;
                    workerNeeded += 2;
                }
            }
        }

        if (currentMarinedAmount == marineNeeded &&
            currentWorkerAmount >= workerNeeded &&
            currentBarrackAmount == barrackNeeded &&
            currentTurretAmount == turretNeeded) {

            if (workerCoolDown == 0 && marineCoolDown == 0) {

                String nextstrategy = strateges[Greenfoot.getRandomNumber(strateges.length)];

                if (!nextstrategy.equals(strategy)) {
                    previousStrategy = strategy;
                    strategy = nextstrategy;
                    spawn();
                }
            }
        }
    }

    private void cleanWorkers(){
        for (int i = 0; i < workers.size(); i++){
            if (workers.get(i) == null || workers.get(i).getWorld() == null){
                workers.remove(i);
                i--;
            }
        }
    }

    public Worker leastBusyWorker(){
        Worker worker = null;
        int least = Integer.MAX_VALUE;

        for (Worker w : workers){
            if (w != null && w.available() < least){
                least = w.available();
                worker = w;
            }
        }
        return worker;
    }

    private Boolean placeBarrack(Buildings building) {
        building.setStatBarEnabled(false);

        int x1 = (teamId == 0) ? 0 : 800;
        int x2 = (teamId == 0) ? 400 : 1200;
        int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;

        for (int i = 0; i < buildingMaximumAtempt; i++) {

            int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
            int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

            w.addObject(building, xPosition, yPosition);

            if (!building.ifTouchingOthers()) {

                Worker worker = leastBusyWorker();
                if (worker == null) {
                    w.removeObject(building);
                    return false;
                }

                building.setStatBarEnabled(true);
                worker.prepBuild(building, xPosition, yPosition);
                w.removeObject(building);
                return true;
            }

            w.removeObject(building);
        }

        building.setStatBarEnabled(true);
        return false;
    }

    private Boolean placeTurret(Buildings building) {
        building.setStatBarEnabled(false);

        int x1 = (teamId == 0) ? 300 : 600;
        int x2 = (teamId == 0) ? 600 : 900;
        int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;

        for (int i = 0; i < buildingMaximumAtempt; i++) {

            int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
            int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

            w.addObject(building, xPosition, yPosition);

            if (!building.ifTouchingOthers()) {

                Worker worker = leastBusyWorker();
                if (worker == null) {
                    w.removeObject(building);
                    return false;
                }

                building.setStatBarEnabled(true);
                worker.prepBuild(building, xPosition, yPosition);
                w.removeObject(building);
                return true;
            }

            w.removeObject(building);
        }

        building.setStatBarEnabled(true);
        return false;
    }

    public void setUpWorld() {
        System.out.println(teamId + " " + strategy);
        workerNeeded = 0;
        marineNeeded = 0;
        barrackNeeded = 0;
        turretNeeded = 0;

        currentWorkerAmount = 0;
        currentMarinedAmount = 0;
        currentBarrackAmount = 0;
        currentTurretAmount = 0;

        if (strategy.equals("ECO")) {
            workerNeeded = 4;
        } else if (strategy.equals("ATK")) {
            workerNeeded = 1;
            barrackNeeded = 1;
            marineNeeded = 1;
            currentWorkerAmount = 0;
            totalBarracksAmt ++;
        } else if (strategy.equals("DEF")){
            currentTurretAmount = 0;
            workerNeeded = 1;

            turretNeeded = 1;
            currentWorkerAmount = 0;
        } else {
            workerNeeded = 2;
            currentWorkerAmount = 0;

            supplyBotNeeded = 1;
            currentSupplyBotAmount = 0;
        }
    }

    private Boolean isBarrackExist() {
        Boolean isThereBarrack = false;

        for (int i = 0; i < barracks.size(); i++) {
            if (barracks.get(i) == null || barracks.get(i).getWorld() == null) {
                barracks.remove(i);
                i--;
                continue;
            }
            isThereBarrack = true;
        }

        return isThereBarrack;
    }

    private void spawn() {
        System.out.println(teamId + " " + strategy);
        if (strategy.equals("ECO")) {
            workerNeeded = 4;
        } else if (strategy.equals("ATK")) {
            
                currentBarrackAmount = 0;
                barrackNeeded = 1;
                marineNeeded = (3 * totalBarracksAmt);
                currentMarinedAmount = 0;
                
                totalBarracksAmt ++;
        } else if (strategy.equals("DEF")) {
            currentTurretAmount = 0;
            turretNeeded = 1;
            workerNeeded = 2;
            currentWorkerAmount = 0;
        } else {
            workerNeeded = 2;
            currentWorkerAmount = 0;

            supplyBotNeeded = 1;
            currentSupplyBotAmount = 0;
        }
    }

    public void correctBuildingList(Buildings building){
        if (building instanceof Turret){
            defensiveTurrets.add((Turret)building);
        } else if (building instanceof Barrack){
            barracks.add((Barrack)building);
        }
    }

    public void addMoney(int money) {
        resources += money;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return resources;
    }

    public int getResources() {
        return resources;
    }

    public void addResources(int amount) {
        resources += amount;
    }

    public boolean spendMoney(int amount) {
        if (amount > resources) {
            return false;
        }

        resources -= amount;
        return true;
    }

    public void addUnit(People unit) {
        if (unit != null && !units.contains(unit)) {
            units.add(unit);
        }
    }

    public void addBuilding(Buildings building) {
        if (building != null && !buildings.contains(building)) {
            buildings.add(building);
        }
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String Strategy) {
        this.strategy = strategy;
    }

    public List<People> getUnits() {
        return units;
    }

    public List<Buildings> getBuildings() {
        return buildings;
    }

    public boolean hasBuilding(Class<? extends Buildings> buildingType) {
        if (buildingType == null) {
            return false;
        }

        for (Buildings building : buildings) {
            if (building != null && buildingType.isInstance(building) && building.getWorld() != null) {
                return true;
            }
        }

        return false;
    }

    public static String getRandomStrategy() {
        return strateges[Greenfoot.getRandomNumber(3)];
    }

    public void addWorker(Worker w){
        if (w != null && !workers.contains(w)){
            workers.add(w);
        }
    }

    public void addSupplyBot(SupplyBot s) {
        supplybots.add(s);
    }
}
