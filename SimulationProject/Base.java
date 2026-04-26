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
    
    public Base(Team team) {
        super(team, 1500);
        setupImage();
        setImage(img);
    }

    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        if(health <= 0 && getWorld() != null){
            getWorld().addObject(new EndScreen(team.getTeamId()), 600, 400);
        }
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        img = new GreenfootImage(team.getName() + getClass().getName() +  ".png");
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
        return true;
    }
    
    public boolean upgrade() {
        // every upgrade makes its level higher, increases max health by 250 and sets health to max hp
        level++;
    
        setMaxHealth(getMaxHealth() + 150);
        health += 150;
        updateStatBar();
        return true;
    }

    public int getLevel() { // returns current level
        return level;
    }

    public int getMaxHealth() { // returns max health
        return maxHealth;
    }
}
