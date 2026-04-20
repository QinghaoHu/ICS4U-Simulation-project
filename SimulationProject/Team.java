import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Team {
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


    public Team(int teamId, String name, int startingMoney, String strategy) {
        this.teamId = teamId;
        this.name = name;
        this.resources = startingMoney;
        this.units = new ArrayList<People>();
        this.buildings = new ArrayList<Buildings>();
        this.strategy = strategy;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
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
