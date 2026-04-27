public class SimulationConfig {
    private final TeamSetup redSetup;
    private final TeamSetup blueSetup;
    private final boolean supplyDropsEnabled;

    public SimulationConfig(TeamSetup redSetup, TeamSetup blueSetup, boolean supplyDropsEnabled) {
        this.redSetup = redSetup;
        this.blueSetup = blueSetup;
        this.supplyDropsEnabled = supplyDropsEnabled;
    }

    public TeamSetup getRedSetup() {
        return redSetup;
    }

    public TeamSetup getBlueSetup() {
        return blueSetup;
    }

    public boolean isSupplyDropsEnabled() {
        return supplyDropsEnabled;
    }

    public static SimulationConfig defaultConfig() {
        return new SimulationConfig(
            new TeamSetup("ECO", 150, 0, 0, 0, false),
            new TeamSetup("ECO", 150, 0, 0, 0, false),
            true
        );
    }
}
