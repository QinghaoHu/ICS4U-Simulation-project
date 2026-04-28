public class SimulationConfig {
    private final TeamSetup redSetup;
    private final TeamSetup blueSetup;
    private final boolean supplyDropsEnabled;
    private final boolean chaosModeEnabled;

    public SimulationConfig(TeamSetup redSetup, TeamSetup blueSetup, boolean supplyDropsEnabled, boolean chaosModeEnabled) {
        this.redSetup = redSetup;
        this.blueSetup = blueSetup;
        this.supplyDropsEnabled = supplyDropsEnabled;
        this.chaosModeEnabled = chaosModeEnabled;
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
            true, false
        );
    }
}
