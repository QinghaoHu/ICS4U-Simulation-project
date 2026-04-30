import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math; 

/**
 * Heavier marine with a burst shot.
 */
public class Officer extends Soldier
{
    /**
     * Act - do whatever the Officer wants to do. This method is called whenever
     * the 'Act' or 'Run' button 
     * gets pressed in the environment.
     */
    
    
    private int bonusBullets;
    
    public Officer(Team team) {
        super(team);
    
        Team enemy = team.getOpponentTeam();
        int enemyLevel = 1;
    
        if (enemy != null) {
            enemyLevel = enemy.getBase().getLevel();
        }
    
        maxHealth = 250 + (enemyLevel - 1) * 75;
        health = maxHealth;
    
        bonusBullets = enemyLevel / 2;
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
        turnTowards(target.getX(), target.getY());
        double angle = shootAngle(target);
        int X = getX() + (int)(centerDist * 2 * Math.cos(Math.toRadians(angle)));
        int Y = getY() + (int)(centerDist * 2 * Math.sin(Math.toRadians(angle)));
    
        int totalBullets = 3 + bonusBullets;
    
        for (int i = 0; i < totalBullets; i++){
            getWorld().addObject(
                new SoldierBullet(this, angle - 20 + Math.random()*40, 4, 2),
                X,
                Y
            );
        }
    }
}