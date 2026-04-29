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
    // Every team will keep track of the people and buildings it has 
    // and go with different approaches randomly and buy certain people and 
    // buildings if it has the resource to buy it
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
    private ArrayList<Worker> workers;
    private ArrayList<SupplyBot> supplybots;

    private String strategy;
    private static String[] strateges = {"ECO", "ATK", "DEF", "REG"};

    private int workerCoolDown = 0;
    //private int marineCoolDown = 0;
    private int supplyBotCoolDown = 0;
    //private int workerAmt = 0;
    private int workerNeeded;
    private int currentWorkerAmount;
    
    private int baseUpgradeCoolDown = 0;
    
    private int marineNeeded;
    private int currentMarinedAmount;

    private int supplyBotNeeded;

    private World world;
    private int currentBarrackAmount;
    private int barrackNeeded;

    private int currentTurretAmount;
    private int turretNeeded;

    private int barrackCost = 125;
    private int turretCost = 150;
    private int stuckTimer = 0;
    private int lastWorkerAmount = 0;
    private int pendingBarracks = 0;
    private int pendingTurrets = 0;
    private Officer officer = null;
    private int officerCooldown = 0;
    private static final int OFFICER_COOLDOWN = 600; // 10 sec at 60 fps
    private World w;

    private Base base;

    private String previousStrategy = "noStrategy";
    private int currentSupplyBotAmount;

    private int MAX_BARRACKS = 4;

    private boolean strategyStarted = false;

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
        currentSupplyBotAmount = supplybots.size();
        currentWorkerAmount = workers.size();
        currentBarrackAmount = barracks.size();
        currentTurretAmount = defensiveTurrets.size();
        
        if (base != null && base.health <= 500) {
            int totalTurrets = currentTurretAmount + pendingTurrets;
        
            if (totalTurrets < 2) {
                turretNeeded = 2;
            }
        
            if (currentWorkerAmount < 2) {
                workerNeeded = 2;
            }
        }
        
        if (workerCoolDown > 0) workerCoolDown--;
        //if (marineCoolDown > 0) marineCoolDown--;
        if (supplyBotCoolDown > 0) supplyBotCoolDown--;
        
        cleanWorkers();
        cleanSupplyBots();
        spendMoney();
        

        if (workerCoolDown == 0 && resources >= Worker.getCost() && base != null && currentWorkerAmount < workerNeeded) {
            if (base.addPeople()) {
                //workerAmt++;
                currentWorkerAmount = workers.size();
                workerCoolDown = Worker.getMaxWorkerCoolDown(base);
                strategyStarted = true;
            }
        }
        
        if (officerCooldown > 0) {
            officerCooldown--;
        }
        
        handleOfficerSpawn();
        
        if ((currentBarrackAmount + pendingBarracks) < barrackNeeded && (currentBarrackAmount + pendingBarracks) < MAX_BARRACKS) {
            if (resources >= barrackCost) {
                Barrack barrack = new Barrack(this);
                if (placeBarrack(barrack)) {
                    pendingBarracks++;
                    resources -= barrackCost;
                    strategyStarted = true;
                }
            }
        }

        if (isBarrackExist()) {
            if (currentMarinedAmount < marineNeeded) {
                if (resources >= Marine.getCost()) {
                    Barrack barrack = barracks.get(Greenfoot.getRandomNumber(barracks.size()));
                    if (barrack.addPeople()) {
                        currentMarinedAmount++;
                        strategyStarted = true;
                        //marineCoolDown = Marine.getMaxMarineCoolDown();
                    }
                }
            }
        }

        if (currentSupplyBotAmount < supplyBotNeeded) {
            if (supplyBotCoolDown == 0 && resources >= SupplyBot.getCost() && base != null) {
                if (base.addBot()) {
                    currentSupplyBotAmount++;
                    supplyBotCoolDown = SupplyBot.getMaxSupplyBotCoolDown();
                    strategyStarted = true;
                }
            }
        }

        if ((currentTurretAmount + pendingTurrets) < turretNeeded && turretNeeded > 0) {
            if (resources >= turretCost) {
                Turret turret = new Turret(this);
                if (placeTurret(turret)) {
                    pendingTurrets++;
                    resources -= turretCost;
                    workerNeeded += 2;
                    strategyStarted = true;
                }
            }
        }

        if (currentMarinedAmount == marineNeeded &&
            currentWorkerAmount >= workerNeeded &&
            currentBarrackAmount >= barrackNeeded &&
            currentTurretAmount >= turretNeeded &&
            currentSupplyBotAmount >= supplyBotNeeded) {

            if (workerCoolDown == 0 && supplyBotCoolDown == 0) {
                String nextstrategy;
                
                if (workers.size() >= 34) {
                    String[] lateGameStrategies = {"ATK", "DEF", "REG"};
                    nextstrategy = lateGameStrategies[Greenfoot.getRandomNumber(lateGameStrategies.length)];
                } else {
                    nextstrategy = strateges[Greenfoot.getRandomNumber(strateges.length)];
                }

                if (!nextstrategy.equals(strategy)) {
                    previousStrategy = strategy;
                    strategy = nextstrategy;
                    spawn();
                }
            }
        }
        
        if (baseUpgradeCoolDown > 0) {
            baseUpgradeCoolDown--;
        }
        
        if (base != null && resources >= 500 && baseUpgradeCoolDown == 0) {
            resources -= 500;
            base.upgrade();
            baseUpgradeCoolDown = 10;
        }

        if (strategyStarted) {
            boolean progressMade = currentWorkerAmount != lastWorkerAmount;
        
            if (progressMade) {
                stuckTimer = 0;
            } else {
                stuckTimer++;
            }
        
            lastWorkerAmount = currentWorkerAmount;
        
            if (stuckTimer > 600) {
                System.out.println(name + " stuck — forcing strategy reset");
        
                String fallback = strateges[Greenfoot.getRandomNumber(strateges.length)];
                strategy = fallback;
        
                spawn();
                stuckTimer = 0;
            }
        }
    }

    private void cleanWorkers() {
        for (int i =0; i < workers.size(); i++) {
            Worker worker = workers.get(i);
    
            if (worker == null || worker.getWorld() == null) {
                if (worker != null) {
                    pendingBarracks -= worker.getPendingBarracks();
                    pendingTurrets -= worker.getPendingTurrets();
    
                    if (pendingBarracks < 0) {
                        pendingBarracks = 0;
                    }
    
                    if (pendingTurrets < 0) {
                        pendingTurrets = 0;
                    }
                }
    
                workers.remove(i);
                i--;
            }
        }
    }

    public Worker leastBusyWorker() {
        Worker worker = null;
        int least = Integer.MAX_VALUE;

        for (Worker w : workers) {
            if (w != null && w.available() < least) {
                least = w.available();
                worker = w;
            }
        }
        return worker;
    }

    private Boolean placeBarrack(Buildings building) { 
        
        // places a barrack done insuring it doesn't collide with anything else
        // and then assigns that job to a worker to build

        if (teamId == 0) {
            int x1 = 0, x2 = 400;
            int y1 = 0, y2 = 400;

            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

                PlaceHolder placeHolder = new PlaceHolder(building.getImage().getHeight(), building.getImage().getWidth());
                w.addObject(placeHolder, xPosition, yPosition);

                if (!placeHolder.ifTouchingOthers()) {
                    Worker worker = leastBusyWorker();

                    if (worker == null) {
                        w.removeObject(placeHolder);
                        return false;
                    }

                    worker.prepBuild(building, xPosition, yPosition);
                    w.removeObject(placeHolder);
                    return true;
                }

                w.removeObject(placeHolder);
            }
        } else if (teamId == 1) {
            int x1 = 800, x2 = 1200;
            int y1 = 400, y2 = UI.PLAY_AREA_BOTTOM_Y;

            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

                PlaceHolder placeHolder = new PlaceHolder(building.getImage().getHeight(), building.getImage().getWidth());
                w.addObject(placeHolder, xPosition, yPosition);

                if (!placeHolder.ifTouchingOthers()) {
                    Worker worker = leastBusyWorker();

                    if (worker == null) {
                        w.removeObject(placeHolder);
                        return false;
                    }

                    worker.prepBuild(building, xPosition, yPosition);
                    w.removeObject(placeHolder);
                    return true;
                }
                w.removeObject(placeHolder);
            }
        }

        return false;
    }
    
        private void handleOfficerSpawn() {
        if (base == null || base.getWorld() == null) {
            return;
        }
    
        Base enemyBase = getEnemyBase();
    
        if (enemyBase == null) {
            return;
        }
    
        int levelDifference = enemyBase.getLevel() - base.getLevel();
    
        Team enemyTeam = enemyBase.team;
    
        // only spawn if enemy is ahead by 3+ and has 4 barracks
        if (levelDifference >= 3 && enemyTeam != null && enemyTeam.barracks.size() >= 3) {
    
            // officer already alive
            if (officer != null && officer.getWorld() != null) {
                return;
            }
    
            // waiting on cooldown
            if (officerCooldown > 0) {
                return;
            }
    
            Barrack spawnBarrack = getRandomBarrack();
    
            if (spawnBarrack != null) {
                officer = new Officer(this);
                w.addObject(officer, spawnBarrack.getX(), spawnBarrack.getY());
                UI.reportUpgrade(this, "Officer", 1);

                officerCooldown = OFFICER_COOLDOWN;
            }
        }
    }
    
    private Base getEnemyBase() {
        if (w == null) {
            return null;
        }
    
        for (Base b : w.getObjects(Base.class)) {
            if (b != null && b != base) {
                if (b.team != this) {
                    return b;
                }
            }
        }
    
        return null;
    }
    
    private Barrack getRandomBarrack() {
        if (barracks.size() == 0) {
            return null;
        }
    
        return barracks.get(Greenfoot.getRandomNumber(barracks.size()));
    }
    
    private Boolean placeTurret(Buildings building) {
        //building.setStatBarEnabled(false);
        // places a Turret done insuring it doesn't collide with anything else
        // and then assigns that job to a worker to build

        if (teamId == 0) {
            int x1 = 300, x2 = 600;
            int y1 = 400, y2 = UI.PLAY_AREA_BOTTOM_Y;

            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

                PlaceHolder placeHolder = new PlaceHolder(building.getImage().getHeight(), building.getImage().getWidth());
                w.addObject(placeHolder, xPosition, yPosition);

                if (!placeHolder.ifTouchingOthers()) {
                    Worker worker = leastBusyWorker();

                    if (worker == null) {
                        w.removeObject(placeHolder);
                        return false;
                    }

                    worker.prepBuild(building, xPosition, yPosition);
                    w.removeObject(placeHolder);
                    return true;
                }

                w.removeObject(placeHolder);
            }
        } else if (teamId == 1) {
            int x1 = 600, x2 = 900;
            int y1 = 0, y2 = 400;

            for (int i = 0; i < buildingMaximumAtempt; i++) {
                int xPosition = x1 + Greenfoot.getRandomNumber(x2 - x1);
                int yPosition = y1 + Greenfoot.getRandomNumber(y2 - y1);

                PlaceHolder placeHolder = new PlaceHolder(building.getImage().getHeight(), building.getImage().getWidth());
                w.addObject(placeHolder, xPosition, yPosition);

                if (!placeHolder.ifTouchingOthers()) {
                    Worker worker = leastBusyWorker();

                    if (worker == null) {
                        w.removeObject(placeHolder);
                        return false;
                    }

                    worker.prepBuild(building, xPosition, yPosition);
                    w.removeObject(placeHolder);
                    return true;
                }
                w.removeObject(placeHolder);
            }
        }

        return false;
    }

    public void setUpWorld() {
        workerNeeded = 0;
        marineNeeded = 0;
        barrackNeeded = 0;
        turretNeeded = 0;
        supplyBotNeeded = 0;

        currentWorkerAmount = 0;
        currentMarinedAmount = 0;
        currentBarrackAmount = 0;
        currentTurretAmount = 0;
        currentSupplyBotAmount = 0;

        strategyStarted = false;
        stuckTimer = 0;

        if (strategy.equals("ECO")) {
            workerNeeded = 4;
        } else if (strategy.equals("ATK")) {
            barrackNeeded = 1;
            marineNeeded = 1;
            workerNeeded = 1;
        } else if (strategy.equals("DEF")) {
            turretNeeded = 1;
            workerNeeded = 1;
        } else if (strategy.equals("REG")) {
            workerNeeded = 2;
            supplyBotNeeded = 1;
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
        workerNeeded = 0;
        marineNeeded = 0;
        barrackNeeded = 0;
        turretNeeded = 0;
        supplyBotNeeded = 0;

        currentWorkerAmount = workers.size();
        currentMarinedAmount = 0;
        currentBarrackAmount = barracks.size();
        currentTurretAmount = defensiveTurrets.size();
        currentSupplyBotAmount = supplybots.size();

        stuckTimer = 0;
        lastWorkerAmount = currentWorkerAmount;

        if (strategy.equals("ECO")) {
            workerNeeded = currentWorkerAmount + 3;
            return;
        } else if (strategy.equals("ATK")) {
            workerNeeded = currentWorkerAmount + 1;
            if ((barracks.size() + pendingBarracks) < MAX_BARRACKS) {
                barrackNeeded = barracks.size() + pendingBarracks + 1;
            } else {
                barrackNeeded = MAX_BARRACKS;
            }
            marineNeeded = (barracks.size() + pendingBarracks) * 2;
        } else if (strategy.equals("DEF")) {
            turretNeeded = defensiveTurrets.size() + pendingTurrets + 1;
        } else if (strategy.equals("REG")) {
            supplyBotNeeded = 1;
            workerNeeded = currentWorkerAmount + 1;
        }
    }

    private void spendMoney() {
        if (resources >= Marine.getCost() * 2 && barracks.size() >= 3) {
            marineNeeded++;
        }
    }   

    public void correctBuildingList(Buildings building) {
        if (building instanceof Turret) {
            if (!defensiveTurrets.contains((Turret) building)) {
                defensiveTurrets.add((Turret) building);

                if (pendingTurrets > 0) {
                    pendingTurrets--;
                }
            }
        } else if (building instanceof Barrack) {
            if (!barracks.contains((Barrack) building)) {
                barracks.add((Barrack) building);

                if (pendingBarracks > 0) {
                    pendingBarracks--;
                }
            }
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
        this.strategy = Strategy;
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
        return strateges[Greenfoot.getRandomNumber(4)];
    }

    public void addWorker(Worker w) {
        if (w != null && !workers.contains(w)) {
            workers.add(w);
            //workerAmt ++;
        }
    }
    
    public void spawnGoldBot() {
        if (base == null || base.getWorld() == null) {
            return;
        }
    
        GoldBot bot = new GoldBot(this, base);
        base.getWorld().addObject(bot, base.getX(), base.getY());
    }
    
    private void cleanSupplyBots() {
        for (int i = 0; i < supplybots.size(); i++) {
            if (supplybots.get(i) == null || supplybots.get(i).getWorld() == null) {
                supplybots.remove(i);
                i--;
            }
        }
    }

    public void addSupplyBot(SupplyBot s) {
        if (s != null && !supplybots.contains(s)) {
            supplybots.add(s);
        }
    }
    
    public int getWorkerCount() {
        return workers.size();
    }

    public Base getBase() {
        return base;
    }
}