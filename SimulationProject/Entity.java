import greenfoot.*;
import java.util.ArrayList;

// The superclass for actors that can die, have health, and wants to a move to a specific position

public abstract class Entity extends SuperSmoothMover{
    protected Team team;
    protected int health;
    protected int maxHealth;
    protected int[] targetPosition = new int[]{0, 0};
    
    private ArrayList<Entity> enemies;

    public Entity(Team team){
        // sets the team of the entity
        setTeam(team);
    }
    
    public Entity(){
        
    }
    
    public void act(){
        if (health <= 0 && getWorld() != null) {
            if (this instanceof Buildings) {
                ((Buildings) this).remove();
                return;
            }
            getWorld().removeObject(this);
            return; // if the entity has less than or equal to 0 health then remove it from the world
        }
    }
    
    public void damage(int d){
        health -= d; // removes health from an entity
    }
    
    public Entity findTarget(int range) // find the closest enttiy that is also not on its team with a specific range
    { 
        Entity closest = null; 
        double closestDist = range;
    
        for (Object obj : getObjectsInRange(range, Actor.class)) {
            if (!(obj instanceof Entity)) continue;
    
            Entity e = (Entity) obj;
    
            if (isOpponent(e)) {
                double dist = Math.hypot(e.getX() - getX(), e.getY() - getY());
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = e;
                }
            }
        }

        return closest;
    }
    
    public Entity findTarget() { // finds closest entity on the other team with infinite range
        enemies = (ArrayList<Entity>)getWorld().getObjects(Entity.class);
        Entity closest = null;
        double shortestDist = 0;
        for (Entity e : enemies) {
            if (!e.getTeam().equals(this.getTeam())) {
                double distX = e.getX() - this.getX();
                double distY = e.getY() - this.getY();
                double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                if (shortestDist < distance) {
                    closest = e;
                }
            }
        }
        return closest;
    }


    public boolean isAlive(){ // returns if the entity has more than 0 hp
        return health > 0;
    }

    public Team getTeam(){ // returns what team it is on
        return team;
    }
    
    public int getHealth() { // returns health
        return health;
    }
    
    public int getMaxHealth() { // returns max health
        return maxHealth;
    }

    public int getTeamId() { // returns what team it is on
        if (team == null) {
            return -1;
        }

        return team.getTeamId();
    }

    public void setTeam(Team team) { // sets the team it is on
        this.team = team;
    }

    public boolean isOpponent(Entity other) { // returns if the entity is not on the same team 
        if (other == null || getTeam() == null || other.getTeam() == null) {
            return false;
        }

        return getTeamId() != other.getTeamId();
    }
}
