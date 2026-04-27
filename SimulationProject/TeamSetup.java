public class TeamSetup{
    private final String strategy;
    private final int startingResources;
    private final int extraWorkers;
    private final int extraBarracks;
    private final int extraTurrets;
    private final boolean supplyBotsEnabled;

    public TeamSetup(String strategy, int startingResources, int extraWorkers, int extraBarracks, int extraTurrets, boolean supplyBotsEnabled) {
        this.strategy = strategy;
        this.startingResources = startingResources;
        this.extraWorkers = extraWorkers;
        this.extraBarracks = extraBarracks;
        this.extraTurrets = extraTurrets;
        this.supplyBotsEnabled = supplyBotsEnabled;
    }

    public String getStrategy() {
        return strategy;
    }

    public int getStartingResources() {
        return startingResources;
    }

    public int getExtraWorkers() {
        return extraWorkers;
    }

    public int getExtraBarracks() {
        return extraBarracks;
    }

    public int getExtraTurrets() {
        return extraTurrets;
    }

    public boolean isSupplyBotsEnabled() {
        return supplyBotsEnabled;
    }
}
