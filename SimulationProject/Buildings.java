import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;

/**
 * Write a description of class Buildings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Buildings extends Entity
{
    private boolean hasStatBar;
    private boolean statBarEnabled = true;

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
        super.act();
    }

    protected void addedToWorld(World world) {
        addStatBar(world);
    }

    public void setStatBarEnabled(boolean statBarEnabled) {
        this.statBarEnabled = statBarEnabled;
    }

    public void addStatBar(World world) {
        if (world != null && statBarEnabled && !hasStatBar) {
            world.addObject(new SuperStatBar(maxHealth, health, this, 50, 6, -65), getX(), getY());
            hasStatBar = true;
        }
    }

    public People addPeople(String type){
        if (getWorld() == null || team == null) {
            return null;
        }

        People newPerson = null;

        if ("Officer".equals(type)) {
            newPerson = new Officer(team);
        } else if ("Marine".equals(type)) {
            newPerson = new Marine(team);
        }

        if (newPerson != null) {
            getWorld().addObject(newPerson, getX(), getY());
        }

        return newPerson;
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

        if (intersectingBuilding.isEmpty() && intersectingResources.isEmpty() && intersectingPeoples.isEmpty()) {
            return false;
        }
        return true;
    }
}
