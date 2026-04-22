import greenfoot.*;
import java.util.ArrayList;

public abstract class Entity extends SuperSmoothMover{
    protected Team team;
    protected int health;
    protected int maxHealth;
    protected int cost; 
    protected int[] targetPosition = new int[]{0, 0};
    
    private ArrayList<Entity> enemies;

    public Entity(Team team){
        //this(); im not sure what this does
        setTeam(team);
    }
    
    public Entity(){
        
    }
    
    public void act(){
        if (health <= 0 && getWorld() != null) {
            die();
            return;
        }
    }
    
    protected void die() {
        World world = getWorld();
        if (world != null) {
            world.removeObject(this);
        }
    }
    
    public void damage(int d){
        health -= d; 
    }
    
    public Entity findTarget(int range)
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
    
    public Entity findTarget() {
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


    public boolean isAlive(){
        return health > 0;
    }

    public Team getTeam(){
        return team;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getTeamId() {
        if (team == null) {
            return -1;
        }

        return team.getTeamId();
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isOpponent(Entity other) {
        if (other == null || getTeam() == null || other.getTeam() == null) {
            return false;
        }

        return getTeamId() != other.getTeamId();
    }
}
