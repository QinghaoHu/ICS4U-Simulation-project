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

    private final int teamId;
    private final String name;
    private int resources;
    private final ArrayList<People> units;
    private final ArrayList<Buildings> buildings;
    private ArrayList<Barrack> barracks;
    private ArrayList<Turret> defensiveTurrets;

    private String strategy;
    private String[] strateges = {"ECO", "ATK", "DEF"};
    private int strategyCoolDown = 0;
    private int maxStrategyCoolDown = 120;

    private int workerCoolDown = 0;
    private int maxWorkerCoolDown = 420;

    private int marineCoolDown = 0;
    private int maxMarineCoolDown = 600;

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
    }

    public void act() {
        if (strategyCoolDown > 0) {
            strategyCoolDown--;
        } else {
            strategyCoolDown = maxStrategyCoolDown;
            strategy = strateges[Greenfoot.getRandomNumber(strateges.length)];
            //System.out.println(strategy); don't print anything out on the push
            spawn();
        }

        if (workerCoolDown > 0) {
            workerCoolDown--;
        }
        if (marineCoolDown > 0) {
            marineCoolDown--;
        }
    }

    public void setUpWorld() {
        // When strategy is Equal
        if (strategy.equals("ECO")) {
            for (int i = 0; i < 5; i++) {
                base.addPeople();
            }
            workerCoolDown = maxWorkerCoolDown;
        } else if (strategy.equals("ATK")) {
            Barrack addBarrack = new Barrack(this);
            barracks.add(addBarrack);
            w.addObject(addBarrack, 100 + Greenfoot.getRandomNumber(1001), 100 + Greenfoot.getRandomNumber(601));
            addBarrack.addPeople();
        } else {
            Turret defenseTower = new Turret(this);
//            defensiveTurrets.add(defenseTower);
            w.addObject(defenseTower, 100 + Greenfoot.getRandomNumber(1001), 100 + Greenfoot.getRandomNumber(601));
            base.addPeople();
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
            if (workerCoolDown == 0) {
                base.addPeople();
                workerCoolDown = maxWorkerCoolDown;
            }
        } else if (strategy.equals("ATK")) {
            if (!isBarrackExist()) {
                Barrack newBarrack = new Barrack(this);
                w.addObject(newBarrack, 100 + Greenfoot.getRandomNumber(1001), 100 + Greenfoot.getRandomNumber(601));
                barracks.add(newBarrack);
            } else {
                base.addPeople();
                workerCoolDown = maxWorkerCoolDown;
                for (int i = 0; i < 4; i++) {
                    barracks.get(Greenfoot.getRandomNumber(barracks.size())).addPeople();
                }
            }
        } else {
            Turret defenseTower = new Turret(this);

            w.addObject(defenseTower, 100 + Greenfoot.getRandomNumber(1001), 100 + Greenfoot.getRandomNumber(601));
            base.addPeople();
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
}
