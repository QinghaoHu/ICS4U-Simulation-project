import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Main base building.
 * Spawns workers, buys supply bots, and handles upgrades.
 */
public class Base extends Buildings {
    private GreenfootImage img;
    private final int cost = 100; 
    private int level = 1;

    private Explosion expld;
    
    public Base(Team team) {
        super(team, 750);
        setupImage();
        setImage(img);
    }

    public void act() {
        if(health <= 0 && getWorld() != null){
            // Death animation runs once, then the world swaps over.
            if (expld == null) {
                expld = new Explosion(1, 5, 300, 120, Color.RED);
                getWorld().addObject(expld, this.getX(), this.getY());
                img = new GreenfootImage(2, 2);
                setImage(img);
                GreenfootSound sound = ResourceCache.getSound("BaseExplosion.mp3");
                sound.setVolume(100);
                sound.play();
            }
            if (Explosion.getIsInUse()) {
                return;
            }
            getWorld().stopped();
            Greenfoot.setWorld(new EndingWorld(team.getTeamId()));
        }
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        img = ResourceCache.getImage(team.getName() + getClass().getName() +  ".png");
        img.scale(115, 115);

        if (img != null) {
            setImage(img);
        }
    }
    
    public int getCost(){
        return 100; 
    }

    public boolean addPeople() {
        // Worker purchase path.
        if (getWorld() == null) {
            return false;
        }
        Worker worker = new Worker(team, this);
        if (!team.spendMoney(worker.getCost())){
            return false;
        }
        team.addWorker(worker);
        
        getWorld().addObject(worker, getX(), getY());
        UI.reportUpgrade(team, "Worker", 1);
        return true;
    }

    public boolean addBot() {
        // Supply bots are the other base buy.
        if (getWorld() == null) {
            return false;
        }
        SupplyBot supplybot = new SupplyBot(team);
        if (!team.spendMoney(SupplyBot.getCost())) {
            return false;
        }
        team.addSupplyBot(supplybot);

        getWorld().addObject(supplybot, getX(), getY());
        UI.reportUpgrade(team, "SupplyBot", 1);
        return true;
    }
    
    public boolean upgrade() {
        // Base upgrades also feed into nearby unit scaling.
        level++;
        UI.reportUpgrade(team, "Base", 1);
    
        setMaxHealth(getMaxHealth() + 75);
        health += 75;
        updateStatBar();
    
        for (Buildings building : team.getBuildings()) {
            if (building instanceof Turret) {
                Turret turret = (Turret) building;
                turret.upgradeTurret();
            }
        }
        
        for (People unit : team.getUnits()) {
            if (unit instanceof SupplyBot) {
                SupplyBot supplyBot = (SupplyBot) unit;
                supplyBot.upgradeCurrentBot();
            }
        }
        
        // New bots should not stay at the old cap.
        SupplyBot.upgradeSupplyBot();
        
        return true;
    }

    public int getLevel() { // current base tier
        return level;
    }

    public int getMaxHealth() { // max HP after upgrades
        return maxHealth;
    }
}
