import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;

/**
 * This is the superclass for all entities that are buildings
 * The shared trait that all buildings will not be not spawn touching another building
 * and spawning people (except for turret) 
 */
public abstract class Buildings extends Entity
{
    private boolean hasStatBar;
    private boolean statBarEnabled = true;
    private SuperStatBar statBar;

    public Buildings(Team team, int maxHealth) {
        super(team);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        
        if (team != null) {
            team.addBuilding(this);
        }
        
        sounds.put("explode", new GreenfootSound(this.getClass().getName() + "Explosion.mp3"));
    }

    /**
     * Act - do whatever the Buildings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        super.act();
    
        if (statBar != null) {
            statBar.setMaxVal(maxHealth);
            statBar.update(health);
        }
    }

    protected void addedToWorld(World world) {
        addStatBar(world);
    }

    public void setStatBarEnabled(boolean statBarEnabled) {
        this.statBarEnabled = statBarEnabled;
    }

    public void addStatBar(World world) {
        if (world != null && statBarEnabled && !hasStatBar) {
            statBar = new SuperStatBar(maxHealth, health, this, 50, 6, -65);
            world.addObject(statBar, getX(), getY());
            hasStatBar = true;
        }
    }

    public abstract int getCost();
    
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

    public boolean ifTouchingOthers() {
        ArrayList<Buildings> intersectingBuilding = (ArrayList<Buildings>) getIntersectingObjects(Buildings.class);
        ArrayList<Resources> intersectingResources = (ArrayList<Resources>) getIntersectingObjects(Resources.class);
        ArrayList<People> intersectingPeoples = (ArrayList<People>) getIntersectingObjects(People.class);
        // checks if the building is touching other things to ensure where it spawns makes sense
        if (intersectingBuilding.isEmpty() && intersectingResources.isEmpty() && intersectingPeoples.isEmpty()) {
            return false;
        }
        return true;
    }
    
        public void setMaxHealth(int newMax) {
        maxHealth = newMax;
    }
    
    public void updateStatBar() {
        if (statBar != null) {
            statBar.setMaxVal(maxHealth);
            statBar.update(health);
        }
    }
    
    public void healToFull() {
        health = maxHealth;
        updateStatBar();
    }
}
