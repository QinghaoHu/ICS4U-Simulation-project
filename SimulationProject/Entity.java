import greenfoot.*;

public abstract class Entity extends SuperSmoothMover{
    protected boolean isAlive;
    protected Team team;

    public Entity(){
        isAlive = true;
    }

    
    
    public Entity findTarget()
    {
        Entity closest = null;
        double closestDist = 200;
    
        for (Object obj : getObjectsInRange(200, Actor.class)) {
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
    
    public Entity(Team team){
        this();
        setTeam(team);
    }

    public boolean isAlive(){
        return isAlive;
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
