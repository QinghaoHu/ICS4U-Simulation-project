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
    
    private Entity e;
    
    public void act()
    {
        e = (Entity)getOneIntersectingObject(Entity.class);
        if(isAtEdge() || e != null){
            getWorld().removeObject(this);
        }
    }
}
