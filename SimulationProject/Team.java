import java.util.ArrayList;
import java.util.List;

public class Team {
    public static final int RED = 0;
    public static final int BLUE = 1;

    private final int teamId;
    private final String name;
    private int money;
    private final ArrayList<People> units;
    private final ArrayList<Buildings> buildings;

    public Team(int teamId, String name, int startingMoney) {
        this.teamId = teamId;
        this.name = name;
        this.money = startingMoney;
        this.units = new ArrayList<People>();
        this.buildings = new ArrayList<Buildings>();
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean spendMoney(int amount) {
        if (amount > money) {
            return false;
        }

        money -= amount;
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

    public List<People> getUnits() {
        return units;
    }

    public List<Buildings> getBuildings() {
        return buildings;
    }
}
