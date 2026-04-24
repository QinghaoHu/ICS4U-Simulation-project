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

    private String strategy;
    private static String[] strateges = {"ECO", "ATK", "DEF"};

    private int workerCoolDown = 0;
    private int maxWorkerCoolDown = 480;

    private int marineCoolDown = 0;
    private int maxMarineCoolDown = 240;

    private int workerNeeded;
    private int currentWorkerAmount;

    private int marineNeeded;
    private int currentMarinedAmount;

    private World world;
    private int currentBarrackAmount;
    private int barrackNeeded;
    private int totalBarracksAmt;

    private int currentTurretAmount;
    private int turretNeeded;

    private int workerCost = 75;
    private int marineCost = 50;
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
        this.workers = new ArrayList<>();

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

    }

    public void act() {
        if (workerCoolDown > 0) {
            workerCoolDown--;
        }
        if (marineCoolDown > 0) {
            marineCoolDown--;
        }

        if (currentWorkerAmount < workerNeeded) {
            if (workerCoolDown == 0 && resources >= workerCost && base != null && base.addPeople()) {
                currentWorkerAmount++;
                workerCoolDown = maxWorkerCoolDown;
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
                if (marineCoolDown == 0 && resources >= marineCost) {
                    Barrack barrack = barracks.get(Greenfoot.getRandomNumber(barracks.size()));
                    if (barrack.addPeople()) {
                        currentMarinedAmount++;
                        marineCoolDown = maxMarineCoolDown;
                    }
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
                }
            }
        }

        if (currentMarinedAmount == marineNeeded && currentWorkerAmount == workerNeeded && currentBarrackAmount == barrackNeeded && currentTurretAmount == turretNeeded) {
            if (workerCoolDown == 0 && marineCoolDown == 0) {
                for (int i = 0; i < 200; i++) {
                    String nextstrategy = strateges[Greenfoot.getRandomNumber(strateges.length)];
                    if (!nextstrategy.equals(previousStrategy) || !nextstrategy.equals(strategy)) {
                        previousStrategy = strategy;
                        strategy = nextstrategy;
                        spawn();
                        break;
                    }
                }
            }
        }
        
    }
    
    public Worker leastBusyWorker(){
        Worker worker = null;
        int least = Integer.MAX_VALUE; 
        for (Worker w: workers){
            if (w.available() < least){
                least = w.available();
                worker = w; 
            }
        }
        return worker;
    }

    private Boolean placeBarrack(Buildings building) {
        building.setStatBarEnabled(false);
        if (teamId == 0) {
            int x1 = 0, x2 = 400;
            int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;
            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);
                w.addObject(building, xPosition, yPosition);
                if (!building.ifTouchingOthers()) {
                    building.setStatBarEnabled(true);
                    leastBusyWorker().prepBuild(building, xPosition, yPosition);
                    w.removeObject(building);
                    return true;
                }
                w.removeObject(building);
            }
        } else if (teamId == 1) {
            int x1 = 800, x2 = 1200;
            int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;
            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);
                w.addObject(building, xPosition, yPosition);
                if(!building.ifTouchingOthers() && leastBusyWorker() != null){
                    building.setStatBarEnabled(true);
                    leastBusyWorker().prepBuild(building, xPosition, yPosition);
                    w.removeObject(building);
                    return true;
                }
                w.removeObject(building);
            }
        }
        building.setStatBarEnabled(true);
        return false;
    }

    private Boolean placeTurret(Buildings building) {
        building.setStatBarEnabled(false);
        if (teamId == 0) {
            int x1 = 300, x2 = 600;
            int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;
            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);
                w.addObject(building, xPosition, yPosition);
                if (!building.ifTouchingOthers() && leastBusyWorker() != null) {
                    building.setStatBarEnabled(true);
                    leastBusyWorker().prepBuild(building, xPosition, yPosition);
                    w.removeObject(building);
                    return true;
                }
                w.removeObject(building);
            }
        } else if (teamId == 1) {
            int x1 = 600, x2 = 900;
            int y1 = 0, y2 = UI.PLAY_AREA_BOTTOM_Y;
            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);
                w.addObject(building, xPosition, yPosition);
                if(!building.ifTouchingOthers()){
                    building.setStatBarEnabled(true);
                    leastBusyWorker().prepBuild(building, xPosition, yPosition);
                    w.removeObject(building);
                    return true;
                }
                w.removeObject(building);
            }
        }
        building.setStatBarEnabled(true);
        return false;
    }

    public void setUpWorld() {
        System.out.println(strategy);
        
        // When strategy is Equal
        if (strategy.equals("ECO")) {
            currentWorkerAmount = 0;
            workerNeeded = 4;
        } else if (strategy.equals("ATK")) {
            currentBarrackAmount = 0;
            workerNeeded = 1;
            barrackNeeded = 1;

            marineNeeded = 1;
            currentWorkerAmount = 0;
            totalBarracksAmt ++;
        } else {
            currentTurretAmount = 0;
            workerNeeded = 1;
            turretNeeded = 1;

            currentWorkerAmount = 0;
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
        System.out.println(strategy);
        System.out.println(totalBarracksAmt);
        if (strategy.equals("ECO")) {
            currentWorkerAmount = 0;
            workerNeeded = 4;
        } else if (strategy.equals("ATK")) {
            
                currentBarrackAmount = 0;
                barrackNeeded = 1;
                marineNeeded = (3 * totalBarracksAmt);
                currentMarinedAmount = 0;
                
                totalBarracksAmt ++;
        } else {
            currentTurretAmount = 0;
            turretNeeded = 1;

            workerNeeded = 2;
            currentWorkerAmount = 0;
        }
    }
    
    public void correctBuildingList(Buildings building){
        if (building instanceof Turret){
            defensiveTurrets.add((Turret)building);
        }else if (building instanceof Barrack){
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
        workers.add(w); 
    }
}
