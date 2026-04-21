import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math; 

/**
 * Write a description of class Officer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Officer extends Soldier
{
    /**
     * Act - do whatever the Officer wants to do. This method is called whenever
     * the 'Act' or 'Run' button 
     * gets pressed in the environment.
     */
    
    public Officer(Team team) {
        super(team);
        if (team != null) {
            team.addUnit(this);
        }
    }
    
    public void act()
    {
        super.act();
        // Add your action code here.
    }
    
    protected void shoot(Entity target){
        double angle = shootAngle(target);
        for (int i=0;i<5;i++){
            getWorld().addObject(new SoldierBullet(this, angle -20 + Math.random()*40, 2.5, 2), getX(), getY()); // adds bullet at an angle
        }
    }
}
