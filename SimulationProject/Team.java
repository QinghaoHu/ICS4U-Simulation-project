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

    private World w;

    private Base base;

    public Team(int teamId, String name, int startingMoney, String strategy, World w) {
        this.teamId = teamId;
        this.name = name;
        this.resources = startingMoney;
        this.units = new ArrayList<People>();
        this.buildings = new ArrayList<Buildings>();

        this.barracks = new ArrayList<Barrack>();
        this.defensiveTurrets = new ArrayList<Turret>();

        this.strategy = strategy;
        this.w = w;

        workerNeeded = 0;
        currentWorkerAmount = 0;

        marineNeeded = 0;
        currentWorkerAmount = 0;
    }

    public void act() {
        isBarrackExist();

        if (workerCoolDown > 0) {
            workerCoolDown--;
        }
        if (marineCoolDown > 0) {
            marineCoolDown--;
        }

        if (currentWorkerAmount < workerNeeded) {
            if (workerCoolDown == 0) {
                base.addPeople();
                currentWorkerAmount++;
                workerCoolDown = maxWorkerCoolDown;
            }
        }

        if (currentMarinedAmount < marineNeeded) {
            if (marineCoolDown == 0) {
                currentMarinedAmount++;
                barracks.get(Greenfoot.getRandomNumber(barracks.size())).addPeople();
                marineCoolDown = maxMarineCoolDown;
            }
        }

        if (currentMarinedAmount == marineNeeded && currentWorkerAmount == workerNeeded) {
            strategy = strateges[Greenfoot.getRandomNumber(3)];
            System.out.println(teamId + ": " + strategy);
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
            if (placeBuilding(addBarrack)) {
                barracks.add(addBarrack);
            }
            marineNeeded = 1;
            currentWorkerAmount = 0;
        } else {
            DefensiveTurret defenseTower = new DefensiveTurret(this);
            if (placeBuilding(defenseTower)) {
                defensiveTurrets.add(defenseTower);
            }
            workerNeeded = 1;
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
        if (strategy.equals("ECO")) {
            currentWorkerAmount = 0;
            workerNeeded = 5;
        } else if (strategy.equals("ATK")) {
            if (!isBarrackExist()) {
                Barrack newBarrack = new Barrack(this);
                if (placeBuilding(newBarrack)) {
                    barracks.add(newBarrack);
                }
                marineNeeded = 1;
                currentMarinedAmount = 0;
            } else {
                workerNeeded = 1;
                currentWorkerAmount = 0;
                marineNeeded = 4;
                currentMarinedAmount = 0;
            }
        } else {
            Turret defenseTower = new Turret(this);

            if (placeBuilding(defenseTower)) {
                defensiveTurrets.add(defenseTower);
            }
            workerNeeded = 1;
            currentWorkerAmount = 0;
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
        if (w == null || building == null || building.getImage() == null) {
            buildings.remove(building);
            return false;
        }

        if (!canPlaceBuildingAt(building, x, y)) {
            buildings.remove(building);
            return false;
        }

        w.addObject(building, x, y);
        return true;
    }

    private int[] findValidBuildingLocation(Buildings building) {
        if (w == null || building == null || building.getImage() == null) {
            return null;
        }

        int width = building.getImage().getWidth();
        int height = building.getImage().getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int redAreaRightX = w.getWidth() / 3;
        int blueAreaLeftX = w.getWidth() * 2 / 3;

        int minX = halfWidth;
        int maxX = w.getWidth() - halfWidth;
        if (teamId == RED) {
            maxX = Math.min(maxX, redAreaRightX - halfWidth);
        } else if (teamId == BLUE) {
            minX = Math.max(minX, blueAreaLeftX + halfWidth);
        }

        int minY = halfHeight;
        int maxY = Math.min(w.getHeight() - halfHeight, UI.PLAY_AREA_BOTTOM_Y - halfHeight);
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

        for (Buildings other : w.getObjects(Buildings.class)) {
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
        int redAreaRightX = w.getWidth() / 3;
        int blueAreaLeftX = w.getWidth() * 2 / 3;

        if (left < 0 || right > w.getWidth() || top < 0 || bottom > UI.PLAY_AREA_BOTTOM_Y) {
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
}
