import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math; 

/**
 * Heavier marine with a burst shot.
 */
public class Officer extends Soldier
{
    public Officer(Team team) {
        super(team);
        
        maxHealth = 550;
        health = 550;
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void setupImage() {
        if (team == null) {
            return;
        }

        // Bigger frame, same team palette.
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
        // Short spread shot, not a single bullet.
        double angle = shootAngle(target);
        for (int i=0;i<5;i++){
            getWorld().addObject(new SoldierBullet(this, angle -20 + Math.random()*40, 5, 6), getX(), getY()); // adds bullet at an angle
        }
    }
}
