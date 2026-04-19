import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Write a description of class Soldier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Soldier extends People
{
    public Soldier(Team team) {
        super(team, 30);
        if (team != null) {
            team.addUnit(this);
        }
    }
    /**
     * Act - do whatever the Soldier wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        
    }
    
    public double shootAngle(Entity e){
        double xDiff = getX() - e.getX(); // gets the difference in x between soldier and entity
        double yDiff = getY() - e.getY(); // gets the difference in y between soldier and entity
        
        double angleRad = Math.atan2(yDiff, xDiff); // gets the angle of soldier and entity in radians
        double angleDeg = Math.toDegrees(angleRad); // converts angle from rad to degrees
        
        return angleDeg; 
    }
}
