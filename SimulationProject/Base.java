import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 *
 * @author (your name)
 * @version (a version number or a date)
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

    /**¡™
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        if(health <= 0 && getWorld() != null){
            if (expld == null) {
                expld = new Explosion(1, 30, 300, 20, Color.RED);
                getWorld().addObject(expld, this.getX(), this.getY());
                img = new GreenfootImage(2, 2);
                setImage(img);
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
        // adds a worker into the world if the team can afford to buy the worker
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
        // adds a bot to the world if the team can afford it
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
        // every upgrade makes its level higher, increases max health by 250 and sets health to max hp
        level++;
    
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
        
        SupplyBot.upgradeSupplyBot();
        
        return true;
    }

    public int getLevel() { // returns current level
        return level;
    }

    public int getMaxHealth() { // returns max health
        return maxHealth;
    }
}