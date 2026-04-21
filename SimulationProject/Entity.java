import greenfoot.*;

public abstract class Entity extends SuperSmoothMover{
    protected Team team;
    protected int health;
    protected int maxHealth;

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
    
    public Entity findTarget()
    {
        return findTarget(200);
    }
    
    public Entity(Team team){
        this();
        setTeam(team);
    }

    public boolean isAlive(){
        return health > 0;
    }

    public Team getTeam(){
        return team;
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
