import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;

/**
 * This is the superclass for all entities that are buildings
 * The shared trait that all buildings will not be not spawn touching another building
 * and spawning people (except for turret) 
 */
public abstract class Buildings extends Entity
{
//    private boolean hasStatBar;
//    private boolean statBarEnabled = true;
    private SuperStatBar statBar;

    public Buildings(Team team, int maxHealth) {
        super(team);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        
        if (team != null) {
            team.addBuilding(this);
        }
        
        // Each building uses its own break sound.
        sounds.put("explode", ResourceCache.getSound(this.getClass().getName() + "Explosion.mp3"));
        sounds.get("explode").setVolume(15);
    }

    /**
     * Act - do whatever the Buildings wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        super.act();
    
        if (statBar != null) {
            // Keep the bar glued to current health.
            statBar.setMaxVal(maxHealth);
            statBar.update(health);
        }
        
        if (health <= 0) sounds.get("explode").play();
    }

    protected void addedToWorld(World world) {
        addStatBar(world);
    }

//    public void setStatBarEnabled(boolean statBarEnabled) {
//        this.statBarEnabled = statBarEnabled;
//    }

    public void addStatBar(World world) {
        if (world != null) {
            statBar = new SuperStatBar(maxHealth, health, this, 50, 6, -65);
            world.addObject(statBar, getX(), getY());
        }
    }

    public void remove() {
        World world = getWorld();
    
        if (world == null) return;
    
        if (statBar != null && statBar.getWorld() != null) {
            // Clean up the bar with the building.
            world.removeObject(statBar);
        }
    
        world.removeObject(this);
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
        // Only place it where the footprint is clear.
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
