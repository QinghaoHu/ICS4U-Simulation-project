import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Projectile extends SmoothMover
{
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        move(20);
        if(checkEdge()){
            getWorld().removeObject(this);
        }
    }
    protected boolean checkEdge() {
        // Check horizontal bounderies 
        if (getX() > getWorld().getWidth() + 50){
            return true;
        }
        else if (getX() < (- 50)){
            return true;
        }
        
        // Check vertical bounderies 
        if (getY() > getWorld().getHeight() + 50){
            return true;
        }
        else if (getY() < (-50)){
            return true;
        }
        
        return false;
    }
}
