import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Projectile extends SuperSmoothMover
{
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    protected Entity shooter;
    protected int damage;
    protected double angle;
    protected GreenfootImage bulletImage;
    protected double speed;
    
    public void act()
    {
        if(isAtEdge()){
            getWorld().removeObject(this);
            return;
        }
        move(speed);
        if (getWorld() == null) {
            return;
        }
        
        Entity targetHit = (Entity)getOneIntersectingObject(Entity.class);
        if (targetHit != null&& !targetHit.getTeam().equals(shooter.getTeam())) {
            ((Entity)targetHit).damage(damage); // damages entity if hit
            getWorld().removeObject(this);
            return;
        }
    }
}
