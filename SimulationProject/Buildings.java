import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Buildings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Buildings extends Entity
{
    protected int health;
    protected int maxHealth;

    public Buildings(Team team, int maxHealth) {
        super(team);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        
        if (team != null) {
            team.addBuilding(this);
        }
    }

    /**
     * Act - do whatever the Buildings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (health <= 0 && getWorld() != null) {
            die();
            return;
        }
    }

    protected void addedToWorld(World world) {
        if (world != null) {
            world.addObject(new HealthBar(this, - 65), getX(), getY());
        }
    }

    protected void die() {
        World world = getWorld();
        if (world != null) {
            world.removeObject(this);
        }
    }

    public People addPeople(String type){
        if (getWorld() == null || team == null) {
            return null;
        }

        People newPerson = null;

        /*if ("Worker".equals(type)) {
            newPerson = new Worker(team, this);
        } else if ("Soldier".equals(type)) {
            newPerson = new Soldier(team);
        }
        */
        if (newPerson != null) {
            getWorld().addObject(newPerson, getX(), getY());
        }

        return newPerson;
    }
    
    public void takeDamage(int amount) {
        if (amount <= 0) return;
        setHealth(health - amount);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
    
    public void setHealth(int value) {
        health = Math.max(0, Math.min(value, maxHealth));
    }

    public boolean isDead() {
        return health <= 0;
    }
}