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
        
        maxHealth = 550;
        health = 550;
    }
    
    public void act()
    {
        super.act();
        // Add your action code here.
    }
    
    protected void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            emptyImg = ResourceCache.getImage("Red" + getClass().getName() + ".png");
            shootingImg = ResourceCache.getImage("Red" + getClass().getName() + "Recoil.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = ResourceCache.getImage("Blue" + getClass().getName() + ".png");
            shootingImg = ResourceCache.getImage("Blue" + getClass().getName() + "Recoil.png");
        }

        if (emptyImg != null) {
            emptyImg.scale(85, 85);
            setImage(img);
        }
        
        if (shootingImg != null) {
            shootingImg.scale(85, 85);
            setImage(img);
        }
        
        setImage(emptyImg);
    }
    
    protected void shoot(Entity target){
        double angle = shootAngle(target);
        for (int i=0;i<5;i++){
            getWorld().addObject(new SoldierBullet(this, angle -20 + Math.random()*40, 5, 6), getX(), getY()); // adds bullet at an angle
        }
    }
}
