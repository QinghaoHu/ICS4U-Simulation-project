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
    private String strategy;
    private String[] strateges = {"ECO", "ATK", "DEF"};
    private int strategyCoolDown = 0;
    private int maxStrategyCoolDown = 300;

    private int workerCoolDown = 0;
    private int maxWorkerCoolDown = 90;

    private World w;

    private Base base;

    public Team(int teamId, String name, int startingMoney, String strategy, World w) {
        this.teamId = teamId;
        this.name = name;
        this.resources = startingMoney;
        this.units = new ArrayList<People>();
        this.buildings = new ArrayList<Buildings>();
        this.strategy = strategy;
        strategyCoolDown = 300;
        this.w = w;
    }

    public void act() {
        if (strategyCoolDown > 0) {
            strategyCoolDown--;
            return;
        } else {
            strategyCoolDown = maxStrategyCoolDown;
            strategy = strateges[Greenfoot.getRandomNumber(2)];
        }
    }

    public void setUpWorld() {
        // When strategy is Equal
        if (strategy.equals("ECO")) {
            if (teamId == 0) {
                for (int i = 0; i < 3; i++) {
                    Worker worker = new Worker(this, base);
                    addUnit(worker);
                    w.addObject(worker,170, 475);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    Worker worker = new Worker(this, base);
                    addUnit(worker);
                    w.addObject(worker,1020, 145);
                }
            }
        } else if (strategy.equals("ATK")) {
            Barrack addBarrack = new Barrack(this);
            if (teamId == 0) {
                w.addObject(addBarrack, 30, 20);
            } else {
                w.addObject(addBarrack, 1000, 600);
            }

            for (int i = 0; i < 3; i++) {
                Soldier solider = new Soldier(this);
                addUnit(solider);
                w.addObject(solider, addBarrack.getX(), addBarrack.getY());
            }
        } else {
            
        }
    }

    private void spawn() {
        if (strategy.equals("ECO")) {
            if (workerCoolDown == 0) {
                Worker worker = new Worker(this, base);
                addUnit(worker);
                w.addObject(worker,1020, 145);
            }
        } else if (strategy.equals("ATK")) {

        } else {
            
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
}
