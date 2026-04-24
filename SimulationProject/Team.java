import greenfoot.Greenfoot;
import greenfoot.*;

import javax.sound.sampled.SourceDataLine;
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
    private static final int BUILDING_PLACEMENT_ATTEMPTS = 200;
    private static final int BUILDING_PLACEMENT_STEP = 25;
    private static final int BUILDING_PLACEMENT_PADDING = 10;

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
    private int maxWorkerCoolDown = 420;

    private int marineCoolDown = 0;
    private int maxMarineCoolDown = 600;

    private int workerNeeded;
    private int currentWorkerAmount;

    private int marineNeeded;
    private int currentMarinedAmount;

    private World world;
    private int currentBarrackAmount;
    private int barrackNeeded;

    private int currentTurretAmount;
    private int turretNeeded;

    private int workerCost = 50;
    private int marineCost = 75;
    private int barrackCost = 150;
    private int turretCost = 175;

    private World w;

    private Base base;

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

        workerNeeded = 0;
        currentWorkerAmount = 0;

        marineNeeded = 0;
        currentWorkerAmount = 0;

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
            if (workerCoolDown == 0) {
                if (base.addPeople()){
                    currentWorkerAmount++;
                    workerCoolDown = maxWorkerCoolDown;
                }// adds workers
            }
        }

        if (!barracks.isEmpty() && currentMarinedAmount < marineNeeded) {
            if (marineCoolDown == 0) {
                if (barracks.get(Greenfoot.getRandomNumber(barracks.size())).addPeople()){
                    currentMarinedAmount++; // adds marines
                    marineCoolDown = maxMarineCoolDown;
            if (workerCoolDown == 0 && resources >= workerCost) {
                base.addPeople();
                resources -= workerCost;
                currentWorkerAmount++;
                workerCoolDown = maxWorkerCoolDown;
            }
        }

        if (currentBarrackAmount < barrackNeeded) {
            if (resources >= barrackCost) {
                Barrack barrack = new Barrack(this);
                if (placeBuilding(barrack)) {
                    barracks.add(barrack);
                    currentBarrackAmount++;
                    resources -= barrackCost;
                }
            }
        }

        if (isBarrackExist()) {
            if (currentMarinedAmount < marineNeeded) {
                if (marineCoolDown == 0 && resources >= marineCost) {
                    currentMarinedAmount++;
                    barracks.get(Greenfoot.getRandomNumber(barracks.size())).addPeople();
                    marineCoolDown = maxMarineCoolDown;
                    resources -= marineCost;
                }
            }
        }

        if (currentTurretAmount < turretNeeded) {
            if (resources >= turretCost) {
                Turret turret = new Turret(this);
                if (canPlaceTurret(turret)) {
                    defensiveTurrets.add(turret);
                    currentTurretAmount++;
                    resources -= turretCost;
                }
            }
        }

        if (currentMarinedAmount == marineNeeded && currentWorkerAmount == workerNeeded && currentBarrackAmount == barrackNeeded && currentTurretAmount == turretNeeded) {
            strategy = strateges[Greenfoot.getRandomNumber(3)];
            spawn();
        }
    }

    public void setUpWorld() {
        // When strategy is Equal
        if (strategy.equals("ECO")) {
            currentWorkerAmount = 0;
            workerNeeded = 5;
        } else if (strategy.equals("ATK")) {
            Barrack addBarrack = new Barrack(this);
            placeBuilding(addBarrack);
            marineNeeded = 1;
            currentWorkerAmount = 0;
        } else {
            Turret defenseTower = new Turret(this);
            placeBuilding(defenseTower);
            currentBarrackAmount = 0;
            barrackNeeded = 1;

            marineNeeded = 1;
            currentWorkerAmount = 0;
        } else {
            currentTurretAmount = 0;
            turretNeeded = 1;

            workerNeeded = 1;
            currentWorkerAmount = 0;
        }
    }
    
    private Boolean canPlaceTurret(Turret t) {
        int counter = 0;
        Boolean canPlaceTurret = false;
        int [] location = null;
        
        while (true) {
            counter++;
            location = findValidBuildingLocation(t);
            if (location[0] < 600 && location[1] > 300 && teamId == RED) {
                canPlaceTurret = true;
                placeBuilding(t, location[0], location[1]);
                break;
            } else if (location[0] > 600 && location[1] < 400 && teamId == BLUE) {
                canPlaceTurret = true;
                placeBuilding(t, location[0], location[1]);
                break;
            }
            if (counter == 60) {
                break;
            }
        }
        
        return canPlaceTurret;
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
        if (strategy.equals("ECO")) {
            currentWorkerAmount = 0;
            workerNeeded = 5;
        } else if (strategy.equals("ATK")) {
            if (!isBarrackExist()) {
                Barrack newBarrack = new Barrack(this);
                workerPlaceBuilding(newBarrack);
                currentBarrackAmount = 0;
                barrackNeeded = 1;
                barrackNeeded = 1;
                marineNeeded = 1;
                currentMarinedAmount = 0;
            } else {
                workerNeeded = 1;
                currentWorkerAmount = 0;
                marineNeeded = 4;
                currentMarinedAmount = 0;
            }
        } else {
            currentTurretAmount = 0;
            turretNeeded = 1;


            workerPlaceBuilding(defenseTower);
            workerNeeded = 1;
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

    private boolean placeBuilding(Buildings building) {
        int[] location = findValidBuildingLocation(building);

        if (location == null) {
            buildings.remove(building);
            return false;
        }

        return placeBuilding(building, location[0], location[1]);
    }
    
    public boolean placeBuilding(Buildings building, int x, int y) {
        if (world == null || building == null || building.getImage() == null) {
            buildings.remove(building);
            return false;
        }

        if (!canPlaceBuildingAt(building, x, y)) {
            buildings.remove(building);
            return false;
        }

        world.addObject(building, x, y);
        return true;
    }
    
    public boolean workerPlaceBuilding(Buildings building) {
        int[] location = findValidBuildingLocation(building);

        if (location == null) {
            buildings.remove(building);
            return false;
        }

        return workerPlaceBuilding(building, location[0], location[1]) && spendMoney(building.getCost());
    }
    
    public boolean workerPlaceBuilding(Buildings building, int x, int y) {
        if (world == null || building == null || building.getImage() == null) {
            buildings.remove(building);
            return false;
        }

        if (!canPlaceBuildingAt(building, x, y)) {
            buildings.remove(building);
            return false;
        }
        
        Worker worker = leastBusyWorker();
        if (worker == null){
            return false;
        }
        worker.prepBuild(building, x, y); 
        
        return true;
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

    private int[] findValidBuildingLocation(Buildings building) {
        if (world == null || building == null || building.getImage() == null) {
            return null;
        }

        int width = building.getImage().getWidth();
        int height = building.getImage().getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int redAreaRightX = world.getWidth() / 3;
        int blueAreaLeftX = world.getWidth() * 2 / 3;

        int minX = halfWidth;
        int maxX = world.getWidth() - halfWidth;
        if (teamId == RED) {
            maxX = Math.min(maxX, redAreaRightX - halfWidth);
        } else if (teamId == BLUE) {
            minX = Math.max(minX, blueAreaLeftX + halfWidth);
        }

        int minY = halfHeight;
        int maxY = Math.min(world.getHeight() - halfHeight, UI.PLAY_AREA_BOTTOM_Y - halfHeight);
        if (minX > maxX || minY > maxY) {
            return null;
        }

        for (int i = 0; i < BUILDING_PLACEMENT_ATTEMPTS; i++) {
            int x = randomBetween(minX, maxX);
            int y = randomBetween(minY, maxY);

            if (canPlaceBuildingAt(building, x, y)) {
                return new int[]{x, y};
            }
        }

        for (int x = minX; x <= maxX; x += BUILDING_PLACEMENT_STEP) {
            for (int y = minY; y <= maxY; y += BUILDING_PLACEMENT_STEP) {
                if (canPlaceBuildingAt(building, x, y)) {
                    return new int[]{x, y};
                }
            }
        }

        return null;
    }

    private int randomBetween(int min, int max) {
        return min + Greenfoot.getRandomNumber(max - min + 1);
    }

    private boolean canPlaceBuildingAt(Buildings building, int x, int y) {
        if (!isInAllowedBuildingArea(building, x, y)) {
            return false;
        }

        for (Buildings other : world.getObjects(Buildings.class)) {
            if (other != null && other.getImage() != null && overlapsBuilding(building, x, y, other)) {
                return false;
            }
        }

        return true;
    }

    private boolean isInAllowedBuildingArea(Buildings building, int x, int y) {
        int halfWidth = building.getImage().getWidth() / 2;
        int halfHeight = building.getImage().getHeight() / 2;
        int left = x - halfWidth;
        int right = x + halfWidth;
        int top = y - halfHeight;
        int bottom = y + halfHeight;
        int redAreaRightX = world.getWidth() / 3;
        int blueAreaLeftX = world.getWidth() * 2 / 3;

        if (left < 0 || right > world.getWidth() || top < 0 || bottom > UI.PLAY_AREA_BOTTOM_Y) {
            return false;
        }

        if (teamId == RED && right > redAreaRightX) {
            return false;
        }

        if (teamId == BLUE && left < blueAreaLeftX) {
            return false;
        }

        return true;
    }

    private boolean overlapsBuilding(Buildings building, int x, int y, Buildings other) {
        int minHorizontalDistance = (building.getImage().getWidth() + other.getImage().getWidth()) / 2 + BUILDING_PLACEMENT_PADDING;
        int minVerticalDistance = (building.getImage().getHeight() + other.getImage().getHeight()) / 2 + BUILDING_PLACEMENT_PADDING;

        return Math.abs(x - other.getX()) < minHorizontalDistance
                && Math.abs(y - other.getY()) < minVerticalDistance;
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
