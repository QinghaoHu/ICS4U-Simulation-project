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
    private GreenfootImage img;
    private GreenfootImage emptyImg;
    private GreenfootImage shootingImg;
    private int counter;
    
    public Soldier(Team team) {
        super(team, 30);
        if (team != null) {
            team.addUnit(this);
        }
        setupImage();
    }
    /**
     * Act - do whatever the Soldier wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act(){
        counter++;
        Entity target = findTarget();
    
        if (target != null && counter % 30 == 0) {
            turnTowards(target.getX(), target.getY());
            getWorld().addObject(new SoldierBullet(this, target, shootAngle(target)), getX(), getY());
        }
    }
    
    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            emptyImg = new GreenfootImage("RedMarine.png");
            shootingImg = new GreenfootImage("RedMarineRecoil.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = new GreenfootImage("BlueMarine.png");
            shootingImg = new GreenfootImage("BlueMarineRecoil.png");
        }

        if (emptyImg != null) {
            emptyImg.scale(50, 50);
            setImage(img);
        }
        
        if (shootingImg != null) {
            shootingImg.scale(50, 50);
            setImage(img);
        }
        
        setImage(emptyImg);
    }
    
    public double shootAngle(Entity e){
        double xDiff = getX() - e.getX(); // gets the difference in x between soldier and entity
        double yDiff = getY() - e.getY(); // gets the difference in y between soldier and entity
        
        double angleRad = Math.atan2(yDiff, xDiff); // gets the angle of soldier and entity in radians
        double angleDeg = Math.toDegrees(angleRad); // converts angle from rad to degrees
        
        return angleDeg; 
    }
}
